package com.example.library.controller

import com.example.library.dto.ErrorDTO
import com.example.library.exception.BookNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class BaseController {

    @ExceptionHandler(value = BookNotFoundException:: class)
    fun handleBookNotFoundException(e: BookNotFoundException) : ResponseEntity<ErrorDTO> {
        return ResponseEntity<ErrorDTO>(ErrorDTO(400, e.message), HttpStatus.BAD_REQUEST)
    }
}