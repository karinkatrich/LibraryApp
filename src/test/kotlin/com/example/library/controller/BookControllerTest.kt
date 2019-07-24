package com.example.library.controller

import com.example.library.model.Author
import com.example.library.model.Book
import com.example.library.model.BookGenre
import com.example.library.model.BookRating
import com.example.library.repo.BookRepository
import com.example.library.service.BookService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RunWith(SpringRunner::class)
@WebFluxTest(BookController::class)
@ActiveProfiles("dev")
class BookControllerTest {

    @MockBean
    lateinit var bookService: BookService;

    @MockBean
    lateinit var bookRepository: BookRepository;

    @Autowired
    lateinit var webTestClient: WebTestClient;

    @Test
    fun `List Books - Success`() {
        // Given
        var bookFlux: Flux<Book> = Flux.fromIterable(listOf(getBook()))
        `when`(bookService.findAll()).thenReturn(bookFlux)

        // When
        webTestClient.get().uri("/books")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""[{"id":1,"title":"Avengers","year":2018,"genre":{"name":"Action","description":"Action"},"ratings":[],"cast":[]}]""");

        // Then
        verify(bookService, times(1)).findAll()
    }

    @Test
    fun `Rate Book - Success`() {
        // Given
        var book = getBook()
        book.ratings.add(BookRating("Great", 5, Date()))
        `when`(bookService.rate(1, "Great", 5)).thenReturn(Mono.just(book))

        // When
        webTestClient.put().uri("/books/1/rate?comment=Great&rating=5")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""{"id":1,"title":"Avengers","year":2018,"genre":{"name":"Action","description":"Action"},"ratings":[{"comment":"Great","rating":5}],"cast":[]}""")

        // Then
        verify(bookService, times(1)).rate(1, "Great", 5)
    }

    @Test
    fun `Rate Book - Failure`() {
//        // Given
//        `when`(bookService.rate(2, "Great", 5)).thenReturn(Mono.error(BookNotFoundException.create(2)))
//
//        // When
//        webTestClient.put().uri("/books/2/rate?comment=Great&rating=5")
//                .exchange()
//                .expectStatus().isBadRequest()
//                .expectBody()
//                .json("""{"code":400,"message":"Book by id 2, not found"}""")
//
//        // Then
//        verify(bookService, times(1)).rate(2, "Great", 5)
    }

    @Test
    fun `Get Book by Id - Success`() {
        // Given
        `when`(bookService.findOne(1)).thenReturn(Mono.just(getBook()))

        // When
        webTestClient.get().uri("/books/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""{"id":1,"title":"Avengers","year":2018,"genre":{"name":"Action","description":"Action"},"ratings":[],"cast":[]}""")

        // Then
        verify(bookService, times(1)).findOne(1)
    }

    fun getBook() : Book {
        return Book(1, "Avengers", 2018, BookGenre("Action", "Action"), ArrayList<BookRating>(), ArrayList<Author>())
    }
}





