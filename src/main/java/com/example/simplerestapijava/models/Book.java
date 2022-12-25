package com.example.simplerestapijava.models;

import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;

import lombok.Getter;
import lombok.Setter;

//@Repository
@Entity(name = "BOOK")
@Table(name = "BOOK", schema = "LIBRARY")
public class Book {
    @Id
    @GeneratedValue
    private final UUID id;
    @Column(name="DESCRIPTION", length=255, nullable=false, unique=false)
    private @Getter @Setter String description;
    @Column(name="ISBN", length=255, nullable=false, unique=true)
    private String isbn;
    @Column(name="AUTHOR", length=255, nullable=false, unique=false)
    private @Getter @Setter String author;

    public Book() {
        UUID uuid = UUID.randomUUID();
		this.id = uuid;
    }

    public Book(String ISBN, String author, String description) {
        UUID uuid = UUID.randomUUID();
		this.id = uuid;
        this.setISBN(ISBN);
        this.setAuthor(author);
		this.setDescription(description);
	}

    public void setISBN(String ISBN) {
        this.isbn = ISBN;
    }

    public String getISBN() {
        return this.isbn;
    }

    @Override
    public boolean equals(Object target) {
        if (target == this) {
            return true;
        }

        if (!(target instanceof Book)) {
            return false;
        }

        Book targetBook = (Book) target;
        return this.getISBN().equals(targetBook.getISBN());
    }

    @Override
    public String toString() {
        String formattedObject = String.format(
                "{\"ISBN\": \"%s\", \"author\": \"%s\", \"description\": \"%s\"}",
                this.getISBN(), this.getAuthor(), this.getDescription()
        );
        return formattedObject;
    }
}
