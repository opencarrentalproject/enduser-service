package com.opencarrental.authorizationservice.configuration

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ApiAccessDeniedExceptionHandler : AccessDeniedHandler {

    val log: Logger = LoggerFactory.getLogger(ApiAccessDeniedExceptionHandler::class.java)

    override fun handle(request: HttpServletRequest?, response: HttpServletResponse?, accessDeniedException: AccessDeniedException?) {
        log.error("""Access error $accessDeniedException""")
        
    }
}