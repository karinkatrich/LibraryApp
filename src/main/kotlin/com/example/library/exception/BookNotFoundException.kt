package com.example.library.exception


class BookNotFoundException: Exception {
    constructor(message: String): super(message)

    companion object {
        fun create(id: Int): BookNotFoundException {
            return BookNotFoundException("Book by id $id, not found")
        }
    }
}