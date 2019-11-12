package com.jk.controller;

import com.jk.model.Book;
import com.jk.service.BookService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.Map;

@Controller
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private SolrClient client;

    @RequestMapping("delAll")
    public String delAll(String id){
        try {
            client.deleteById("core1",id);
            client.commit("core1");

            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @RequestMapping("addBookPage")
    public String addBookPage(Book book){

        bookService.addBookPage(book);

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        try {
            SolrInputDocument doc = new SolrInputDocument();
            doc.setField("bookname", book.getBookname());
            doc.setField("bookprice", book.getBookprice(.0));
            doc.setField("booktype", book.getBooktype());

            /* 如果spring.data.solr.host 里面配置到 core了, 那么这里就不需要传 collection1 这个参数
             * 下面都是一样的
             */

            client.add("core1", doc);
            //client.commit();
            client.commit("core1");
            return uuid;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "error";
    }

    @RequestMapping("queryBook")
    @ResponseBody
    public Map<String,Object> queryBook(Book book,Integer page,Integer rows){

        Map<String,Object> map1=new HashMap<>();
        try {
            //存放所有的查询条件
            SolrQuery params = new SolrQuery();

            //查询条件, 这里的 q 对应 下面图片标红的地方
            if(book.getBookname()!=null && !"".equals(book.getBookname())){
                params.set("q", book.getBookname());
            }else {
                params.set("q", "*:*");
            }


            //过滤条件
            // params.set("fq", "carPrice:["+car.get+" TO "++"]");

            //排序
            params.addSort("bookprice", SolrQuery.ORDER.desc);

            //分页
            if(page==null){
                params.setStart(0);
            }else {
                params.setStart((page-1)*rows);
            }
            if(rows==null){
                params.setRows(5);
            }else {
                params.setRows(rows);
            }


            //默认域
            params.set("df", "bookname");

            //只查询指定域
            //params.set("fl", "id,product_title,product_price");

            //高亮
            //打开开关
            params.setHighlight(true);
            //指定高亮域
            params.addHighlightField("bookname");
            //设置前缀
            params.setHighlightSimplePre("<span style='color:red'>");
            //设置后缀
            params.setHighlightSimplePost("</span>");

            //查询后返回的对象
            QueryResponse queryResponse = client.query("core1",params);
            //查询后返回的对象 获得真正的查询结果
            SolrDocumentList results = queryResponse.getResults();
            //查询的总条数
            long numFound = results.getNumFound();

            System.out.println(numFound);

            //获取高亮显示的结果, 高亮显示的结果和查询结果是分开放的
            Map<String, Map<String, List<String>>> highlight = queryResponse.getHighlighting();

            //创建list集合接收我们循环的参数
            List<Book> list1 =new ArrayList<>();
            for (SolrDocument result : results) {

                Book book1=new Book();
                String highFile="";

                Map<String, List<String>> map = highlight.get(result.get("id"));
                List<String> list = map.get("bookname");
                if(list==null){
                    highFile= result.get("bookname").toString();
                }else {
                    highFile=list.get(0);
                }

                book1.setBookid(Integer.parseInt(result.get("id").toString()));
                book1.setBookname(highFile);
                book1.getBookprice(Double.parseDouble(result.get("bookprice").toString()));
                book1.setBooktype(result.get("booktype").toString());
                list1.add(book1);
            }
            map1.put("total",numFound);
            map1.put("rows",list1);
            return map1;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }
}
