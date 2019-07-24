package com.example.library.service

import com.example.library.exception.BookNotFoundException
import com.example.library.model.Book
import com.example.library.model.BookRating
import com.example.library.repo.BookRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class BookService constructor(val bookRepository: BookRepository) {

    fun findAll() = this.bookRepository.findAll()

    fun save(book : Book) : Mono<Book> = this.bookRepository.save(book)

    fun findOne(id : Int) : Mono<Book> = this.bookRepository.findById(id)
            .switchIfEmpty(Mono.error(BookNotFoundException.create(id)))

    fun rate(id : Int, comment : String, rating : Int) : Mono<Book> {
        var bookMono: Mono<Book> = this.findOne(id)
        return bookMono.switchIfEmpty(Mono.error(BookNotFoundException.create(id))).map({ book ->
            book.ratings.add(BookRating(comment, rating, Date()))
            book;
        }).map({ book ->
            this.save(book).subscribe()
            book
        });
    }
}