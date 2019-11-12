package com.jk.service;

import com.jk.mapper.BookMapper;
import com.jk.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public void addBookPage(Book book) {

        bookMapper.addBookPage(book);
    }
}
