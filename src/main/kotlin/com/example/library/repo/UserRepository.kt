package com.example.library.repo

import com.example.library.model.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux


interface UserRepository: ReactiveMongoRepository<User, Int> {
    fun findByUsername(userName: String): Flux<User>;
}