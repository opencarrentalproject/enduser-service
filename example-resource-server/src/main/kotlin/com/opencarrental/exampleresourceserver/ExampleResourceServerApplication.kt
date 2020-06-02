package com.opencarrental.exampleresourceserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExampleResourceServerApplication

fun main(args: Array<String>) {
	runApplication<ExampleResourceServerApplication>(*args)
}
