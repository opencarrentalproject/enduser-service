package com.opencarrental.enduserservice.exception

data class InvalidEndUserException(val errorDetails: List<ErrorDetail>) : RuntimeException()