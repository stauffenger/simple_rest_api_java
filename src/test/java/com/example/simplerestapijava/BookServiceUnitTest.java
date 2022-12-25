package com.example.simplerestapijava;

import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.simplerestapijava.models.Book;
import com.example.simplerestapijava.services.BookServices;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookServiceUnitTest {

    @Autowired
    private BookServices bookService;
    private List<String> insertedISBN;
    private List<String> insertedAuthors;
    private TestInfo testInfo;

    @BeforeEach
    void init(TestInfo testInfo) {
        this.testInfo = testInfo;
        System.out.println(this.testInfo.getDisplayName());
    }

    @BeforeAll
    @DisplayName("Initializing the Book Service Unit Test...")
    public void InitializingBookServiceUnitTest(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
        List<List> insertedLines = getInsertedLinesFromFile("data-test.sql");
        this.insertedISBN = insertedLines.get(0);
        this.insertedAuthors = insertedLines.get(1);
        Assert.assertNotEquals(this.insertedISBN.size(), 0);
    }

    private List<List> getInsertedLinesFromFile(String fileName) {
        List<List> insertedLines = new ArrayList<List>();
        List<String> insertedISBN = new ArrayList<String>();
        List<String> insertedAuthors = new ArrayList<String>();
        try {
            URL targetResource = getClass().getClassLoader().getResource(fileName);
            Scanner scanner = new Scanner(Paths.get(targetResource.toURI()).toFile());

            while (scanner.hasNextLine()) {
                String targetLine = scanner.nextLine();
                String formattedLine = targetLine.toLowerCase().replaceAll(" ", "");
                if (formattedLine.contains("insertintolibrary.book")) {
                    String targetAuthor = targetLine.split("VALUES\\(")[1].split(",")[2].replaceAll("\'", "").trim();
                    String targetISBN = formattedLine.split("insertintolibrary.bookvalues\\(")[1].split(",")[1].replaceAll("\'", "");
                    System.out.println(String.format("Found ISBN - %s and author - %s, on file: %s", targetISBN, targetAuthor, fileName));
                    insertedISBN.add(targetISBN);
                    insertedAuthors.add(targetAuthor);
                }
            }

            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            insertedLines.add(insertedISBN);
            insertedLines.add(insertedAuthors);
            return insertedLines;
        }
    }

    @Test
    @DisplayName("Verifying Initial records creation...")
    public void whenApplicationStarts_thenHibernateCreatesInitialRecords() {
        List<Book> books = bookService.list();
        for (String targetISBN: this.insertedISBN) {
            boolean foundISBN = false;
            for (Book targetBook : books) {
                foundISBN = targetISBN.equals(targetBook.getISBN());
                if (foundISBN) {
                    System.out.println(String.format("Book found. %s", targetBook.toString()));
                    break;
                }
            }

            if (!foundISBN) Assert.fail(String.format("Unable to find the ISBN - %s.", targetISBN));
        }

        Assert.assertEquals(books.size(), this.insertedISBN.size());
    }

    @DisplayName("Registering new books...")
    @ParameterizedTest(name = "Registering book with ISB - {0}, author - {1}, description - {2}.")
    @CsvSource({
            "978-3-5222-9314-4, 'Zero', 'The X Buster'",
            "978-3-5222-9314-5, 'Zero', 'The X Buster'",
            "978-3-5222-9314-6, 'Zero', 'The X Buster'",
            "978-3-5222-9314-7, 'Zero', 'The X Buster'"
    })
    public void registerBooksTest(String ISBN, String author, String description) {
        Book newBook = new Book(ISBN, author, description);
        boolean registered = bookService.register(newBook);
        Assert.assertTrue(registered);

        System.out.println(String.format("Verifying book insertion, ISBN - %s.", ISBN));
        Book foundBook = bookService.getByISBN(ISBN);
        if (foundBook == null) Assert.fail(String.format("Unable to find the book with ISBN - %s.", ISBN));
        System.out.println("Book found.");
        Assert.assertEquals(foundBook.getISBN(), ISBN);
    }

    @Test
    @DisplayName("Getting the books by ISBN...")
    public void getByISBNTest() {
        for (String targetISBN : this.insertedISBN) {
            System.out.println(String.format("Looking for the book with ISBN - %s.", targetISBN));
            Book foundBook = bookService.getByISBN(targetISBN);
            if (foundBook == null) Assert.fail(String.format("Unable to find the book with ISBN - %s.", targetISBN));

            Assert.assertEquals(foundBook.getISBN(), targetISBN);
        }
    }

    @Test
    @DisplayName("Getting the books by author...")
    public void getByAuthorTest() {
        for (String targetAuthor : this.insertedAuthors) {
            System.out.println(String.format("Looking for books with Author - %s.", targetAuthor));
            List<Book> foundBooks = bookService.getByAuthor(targetAuthor);
            if (foundBooks.size() == 0) Assert.fail(String.format("Unable to find a book with Author - %s.", targetAuthor));
            System.out.println("Books found:");
            for (Book targetBook : foundBooks) {
                System.out.println(String.format("- %s", targetBook.toString()));
            }

            Assert.assertNotEquals(foundBooks.size(), 0);
        }
    }
}
