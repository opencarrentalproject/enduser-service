package com.opencarrental.exampleresourceserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer

@SpringBootApplication
class ExampleResourceServerApplication

fun main(args: Array<String>) {
	runApplication<ExampleResourceServerApplication>(*args)
}
