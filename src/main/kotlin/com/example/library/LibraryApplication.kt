package com.example.library

import com.example.library.model.Book
import com.example.library.model.Author
import com.example.library.model.BookGenre
import com.example.library.model.BookRating
import com.example.library.service.BookService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import reactor.core.publisher.Mono
import java.util.*


@SpringBootApplication
class LibraryApplication {
	@Bean
	@Profile("default")
	fun commandLineRunner(bookService: BookService) : CommandLineRunner {
		return CommandLineRunner {
			var book : Book = Book(1, "Titanic", 1999, BookGenre("Romantic", "Romantic"),
					Arrays.asList(BookRating("Good Book", 5, Date())),
					listOf(Author("Lionardo Dicaprio", "Jack", 1), Author("Kate Winslet", "Rose", 2)))
			Mono.just(book).subscribe({book -> bookService.save(book).subscribe()})
		}
	}
}


fun main(args: Array<String>) {
	runApplication<LibraryApplication>(*args)
}
