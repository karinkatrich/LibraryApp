package com.example.library.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "books")
data class Book(@Id val id: Int,
                val title: String,
                val year: Int,
                val genre: BookGenre,
                val ratings: MutableList<BookRating>,
                val cast: List<Author>)

