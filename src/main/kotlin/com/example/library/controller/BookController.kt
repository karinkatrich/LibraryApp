package com.example.library.controller

import com.example.library.service.BookService
import com.example.library.model.Book
import org.springframework.web.bind.annotation.*
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RequestMapping("/books")
@RestController
class BookController constructor(val bookService : BookService) {

    @GetMapping
    fun getBook() : Flux<Book> {
        return this.bookService.findAll();
    }

    @GetMapping("/{id}")
    fun getBook(@PathVariable id : Int) : Mono<Book> {
        return this.bookService.findOne(id);
    }

    @PutMapping("/{id}/rate")
    fun rateBook(@PathVariable id : Int, @RequestParam rating : Int, @RequestParam comment : String) : Mono<Book> {
        return this.bookService.rate(id, comment, rating);
    }
}