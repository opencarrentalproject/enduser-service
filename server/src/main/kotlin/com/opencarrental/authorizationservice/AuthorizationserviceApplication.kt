package com.opencarrental.authorizationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer

// TODO implement jwt validation and sign

@SpringBootApplication
@EnableResourceServer
class AuthorizationserviceApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationserviceApplication>(*args)
}
