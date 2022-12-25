package com.example.simplerestapijava;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.simplerestapijava.models.Book;
import com.example.simplerestapijava.services.BookServices;

@SpringBootTest
@ActiveProfiles("prod")
class SimpleRestApiJavaApplicationTests {

	@Autowired
	private BookServices bookService;

	@Test
	void contextLoads(TestInfo testInfo) {
		System.out.println(testInfo.getDisplayName());
	}

	@Test
	@DisplayName("Verifying Initial records creation...")
	public void whenApplicationStarts_thenHibernateCreatesInitialRecords(TestInfo testInfo) {
		System.out.println(testInfo.getDisplayName());
		List<Book> books = bookService.list();

		Assert.assertEquals(books.size(), 0);
	}

}
