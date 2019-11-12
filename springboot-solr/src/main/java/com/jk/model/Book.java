package com.jk.model;

import java.io.Serializable;

public class Book implements Serializable{


    private static final long serialVersionUID = 8506961101202450805L;

    private Integer bookid;

    private String  bookname;

    private Double  bookprice;

    private String  booktype;

    public Integer getBookid() {
        return bookid;
    }

    public String getBookname() {
        return bookname;
    }

    public Double getBookprice(double bookprice) {
        return this.bookprice;
    }

    public String getBooktype() {
        return booktype;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public void setBookprice(Double bookprice) {
        this.bookprice = bookprice;
    }

    public void setBooktype(String booktype) {
        this.booktype = booktype;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookid=" + bookid +
                ", bookname='" + bookname + '\'' +
                ", bookprice=" + bookprice +
                ", booktype='" + booktype + '\'' +
                '}';
    }
}
