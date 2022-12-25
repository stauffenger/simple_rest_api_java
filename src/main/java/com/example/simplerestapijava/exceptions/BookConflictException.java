package com.example.simplerestapijava.exceptions;

public class BookConflictException extends RuntimeException {
    public BookConflictException(String description) {
        super(String.format("Book already exists '%s'", description));
    }
}