package com.example.simplerestapijava.repositories;

import java.util.UUID;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simplerestapijava.models.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    Book findByIsbn(String ISBN);
    List<Book> findByAuthorContainingIgnoreCase(String author);
}
