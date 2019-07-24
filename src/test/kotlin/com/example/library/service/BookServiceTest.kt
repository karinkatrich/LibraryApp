package com.example.library.service

import com.example.library.exception.BookNotFoundException
import com.example.library.model.Author
import com.example.library.model.Book
import com.example.library.model.BookGenre
import com.example.library.model.BookRating
import com.example.library.repo.BookRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.context.ActiveProfiles
import reactor.core.publisher.Mono

@RunWith(MockitoJUnitRunner::class)
@ActiveProfiles("dev")
class BookServiceTest {

    @Mock
    lateinit var bookRepository: BookRepository

    lateinit var bookService: BookService

    @Before
    fun setup() {
        bookService = BookService(bookRepository)
    }

    @Test
    fun `Saving a Book - Success`() {
        // Given
        var expected = getBook()
        `when`(bookRepository.save(expected)).thenReturn(Mono.just(expected))

        // When
        val actual = bookService.save(expected)

        // Then
        actual.subscribe({book -> assertThat(book).isEqualTo(expected)});
        verify(bookRepository, times(1)).save(expected)
    }

    @Test
    fun `Find a Book by Id - Success`() {
        // Given
        var expected = getBook()
        `when`(bookRepository.findById(1)).thenReturn(Mono.just(expected))

        // When
        val actual = bookService.findOne(1)

        // Then
        actual.subscribe({book -> assertThat(book).isEqualTo(expected)})
        verify(bookRepository, times(1)).findById(1)
    }

    @Test
    fun `Find a Book by Id - Failure`() {
        // Given
        var expected = getBook()
        `when`(bookRepository.findById(1)).thenReturn(Mono.empty())

        // When
        val actual = bookService.findOne(1)

        // Then
        actual.doOnError({t -> assertThat(t).isInstanceOf(BookNotFoundException::class.java)}).subscribe()
        verify(bookRepository, times(1)).findById(1)
    }

    @Test
    fun `Rate a Book - Success`() {
        // Given
        var expected = getBook()
        `when`(bookRepository.findById(1)).thenReturn(Mono.just(expected))
        `when`(bookRepository.save(expected)).thenReturn(Mono.just(expected))

        // When
        var actual = bookService.rate(1, "Great", 5)

        // Then
        actual.subscribe({book -> assertThat(book.ratings).hasSize(1)})
        verify(bookRepository, times(1)).findById(1)
        verify(bookRepository, times(1)).save(expected)
    }

    @Test
    fun `Rate a Book - Failure`() {
        // Given
        `when`(bookRepository.findById(1)).thenReturn(Mono.empty())

        // When
        var actual = bookService.rate(1, "Great", 5)

        // Then
        actual.doOnError({t -> assertThat(t).isInstanceOf(BookNotFoundException::class.java)}).subscribe()
    }

    fun getBook() : Book {
        return Book(1, "Avengers", 2018, BookGenre("Action", "Action"), ArrayList<BookRating>(), ArrayList<Author>())
    }


}