package com.opencarrental.enduserservice

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer


@SpringBootTest
@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
abstract class AbstractIntegrationTest(@Autowired val restTemplate: TestRestTemplate) {

    companion object {
        val mongodbContainter = GenericContainer<Nothing>("mongo:4.0")
                .apply { withExposedPorts(27017) }
    }

    @BeforeEach
    fun setup() {
        restTemplate.restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory()
    }

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
        override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
            mongodbContainter.start()

            TestPropertyValues.of(
                    "spring.data.mongodb.host=${mongodbContainter.containerIpAddress}",
                    "spring.data.mongodb.port=${mongodbContainter.firstMappedPort}"
            ).applyTo(configurableApplicationContext.environment)
        }
    }
}