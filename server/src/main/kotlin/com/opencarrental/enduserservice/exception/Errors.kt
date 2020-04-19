package com.opencarrental.enduserservice.exception

data class ErrorDetail(val field: String, val message: String)

data class InvalidEndUserException(val errorDetails: List<ErrorDetail>) : RuntimeException()

data class NotUniqueUserException(val errorDetails: List<ErrorDetail>) : RuntimeException()

data class EndUserNotFoundException(override val message: String) : RuntimeException(message)