package com.example.library.repo

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import com.example.library.model.Book

interface BookRepository: ReactiveMongoRepository<Book, Int> {

}