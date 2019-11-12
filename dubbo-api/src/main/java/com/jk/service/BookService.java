package com.jk.service;

import com.jk.model.Book;

import java.util.List;

public interface BookService {

    List<Book> queryCarList();


    void addBookPage(Book book);

    void delAll(Integer[] ids);

    Book queryById(Integer id);

    List<Book> queryBook();
}
