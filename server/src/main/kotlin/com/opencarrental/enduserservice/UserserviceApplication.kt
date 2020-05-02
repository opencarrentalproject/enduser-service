package com.opencarrental.enduserservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer

// TODO implement jwt validation and sign

@SpringBootApplication
@EnableResourceServer
class UserserviceApplication

fun main(args: Array<String>) {
    runApplication<UserserviceApplication>(*args)
}
