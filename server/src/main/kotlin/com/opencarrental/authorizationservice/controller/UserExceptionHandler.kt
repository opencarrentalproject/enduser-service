package com.opencarrental.authorizationservice.controller

import com.opencarrental.authorizationservice.exception.EndUserNotFoundException
import com.opencarrental.authorizationservice.exception.InvalidEndUserException
import com.opencarrental.authorizationservice.exception.NotUniqueUserException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@RestControllerAdvice
class UserExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [InvalidEndUserException::class])
    protected fun handleInvalidData(
            ex: InvalidEndUserException, request: WebRequest?): ResponseEntity<Any?>? {
        return handleExceptionInternal(ex, ex.errorDetails,
                HttpHeaders(), HttpStatus.BAD_REQUEST, request!!)
    }

    @ExceptionHandler(value = [NotUniqueUserException::class])
    protected fun handleConflict(ex: NotUniqueUserException, request: WebRequest?): ResponseEntity<Any?>? {
        return handleExceptionInternal(ex, ex.errorDetails,
                HttpHeaders(), HttpStatus.CONFLICT, request!!)
    }

    @ExceptionHandler(value = [EndUserNotFoundException::class])
    protected fun handleNotFound(ex: EndUserNotFoundException, request: WebRequest?): ResponseEntity<Any?>? {
        return handleExceptionInternal(ex, ex.message, HttpHeaders(), HttpStatus.NOT_FOUND, request!!)
    }

    @ExceptionHandler(value = [Exception::class])
    protected fun handleOther(ex: Exception, request: WebRequest?): ResponseEntity<Any?>? {
        return handleExceptionInternal(ex, """Error encountered while processing request. Try again later. ${ex.javaClass}""",
                HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request!!)
    }
}