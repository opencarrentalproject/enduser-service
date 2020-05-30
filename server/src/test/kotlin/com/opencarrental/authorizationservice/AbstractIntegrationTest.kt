package com.opencarrental.authorizationservice

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer


@SpringBootTest
@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
abstract class AbstractIntegrationTest(@Autowired val testRestTemplate: TestRestTemplate) {

    lateinit var token: String


    @Value("\${admin.username}")
    lateinit var adminUserName: String

    @Value("\${admin.password}")
    lateinit var adminPassword: String

    companion object {
        val mongodbContainter = GenericContainer<Nothing>("mongo:4.0")
                .apply { withExposedPorts(27017) }
    }

    @BeforeEach
    fun setup() {
        testRestTemplate.restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory()
        // Cleanup interceptor
        testRestTemplate.restTemplate.interceptors.clear()

        val tokenRequest = HttpEntity<Credentials>(Credentials(adminUserName, adminPassword))
        val response = testRestTemplate.postForEntity("/admin/login", tokenRequest, String::class.java)
        token = response.body
                ?: throw RuntimeException("Failed to retrieve token.Check the security configuration")
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

    internal data class Token(
            val access_token: String
    )

    internal data class Credentials(
            val username: String,
            val password: String
    )
}