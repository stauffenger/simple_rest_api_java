package com.example.simplerestapijava;

import java.util.List;
import java.util.ArrayList;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.simplerestapijava.models.Book;
import com.example.simplerestapijava.exceptions.BookConflictException;

@RestController
@SpringBootApplication
public class BooksController {
	Dotenv dotenv = Dotenv.load();
	private List<Book> books = new ArrayList<Book>();
	public static void main(String[] args) {
		SpringApplication.run(BooksController.class, args);
	}


	@GetMapping("/healthCheck")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s! %s, %s", name, this.dotenv.get("TEST"), this.dotenv.get("TESTE"));
	}

	@ResponseBody
	@GetMapping("/books")
	public ResponseTransfer listBooks() {
		String descriptions = new String();

		return new ResponseTransfer(descriptions);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(path = "/books/insert", produces = "application/json")
	public ResponseTransfer insertBook(@RequestBody Book newBook) {
		if (books.contains(newBook)) {
			throw new BookConflictException(newBook.getDescription());
		}

		this.books.add(newBook);
		return new ResponseTransfer("Success!");
	}
}
