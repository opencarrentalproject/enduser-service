package com.opencarrental.authorizationservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// TODO implement jwt validation and sign

@SpringBootApplication
class AuthorizationserviceApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationserviceApplication>(*args)
}
