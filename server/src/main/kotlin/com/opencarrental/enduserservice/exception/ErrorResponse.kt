package com.opencarrental.enduserservice.exception

data class ErrorResponse(val errorDetail: List<ErrorDetail>)

data class ErrorDetail(val field: String, val message: String)