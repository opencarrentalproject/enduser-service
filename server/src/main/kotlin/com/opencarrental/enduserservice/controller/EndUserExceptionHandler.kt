package com.opencarrental.enduserservice.controller

import com.opencarrental.enduserservice.exception.InvalidEndUserException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class EndUserExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [InvalidEndUserException::class])
    protected fun handleInvalidData(
            ex: InvalidEndUserException, request: WebRequest?): ResponseEntity<Any?>? {
        return handleExceptionInternal(ex, ex.errorDetails,
                HttpHeaders(), HttpStatus.BAD_REQUEST, request!!)
    }
}