package com.example.simplerestapijava.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.simplerestapijava.models.Book;
import com.example.simplerestapijava.repositories.BookRepository;

@Service
public class BookServices {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> list() {
        return bookRepository.findAll();
    }

    public boolean register(Book newBook) {
        Book savedBook = bookRepository.save(newBook);
        if (savedBook != null) return true;

        return false;
    }

    public Book getByISBN(String ISBN) {
        Book foundBook = bookRepository.findByIsbn(ISBN);
        return foundBook;
    }

    public List<Book> getByAuthor(String author) {
        List<Book> foundBooks = bookRepository.findByAuthorContainingIgnoreCase(author);
        return foundBooks;
    }
}
