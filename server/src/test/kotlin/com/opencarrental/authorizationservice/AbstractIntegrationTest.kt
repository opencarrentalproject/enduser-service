package com.opencarrental.authorizationservice

import org.apache.http.entity.ContentType
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.test.context.ContextConfiguration
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.testcontainers.containers.GenericContainer
import java.util.*


@SpringBootTest
@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
abstract class AbstractIntegrationTest(@Autowired val testRestTemplate: TestRestTemplate) {

    lateinit var token: String

    companion object {
        val mongodbContainter = GenericContainer<Nothing>("mongo:4.0")
                .apply { withExposedPorts(27017) }
    }

    @BeforeEach
    fun setup() {
        testRestTemplate.restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory()
        // Cleanup interceptor
        testRestTemplate.restTemplate.interceptors.clear()
        val headers = HttpHeaders()
        val map = mapOf(
                "Authorization" to listOf<String>("Basic ${Base64.getEncoder().encodeToString("admin_client:admin".toByteArray())}"),
                "Content-Type" to listOf<String>(ContentType.APPLICATION_FORM_URLENCODED.mimeType)
        )
        headers.putAll(map)
        val formParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        formParams.add("grant_type", "client_credentials")
        val tokenRequest = HttpEntity<MultiValueMap<String, String>>(formParams, headers)
        val response = testRestTemplate.postForEntity("/oauth/token", tokenRequest, Token::class.java)
        token = response.body?.access_token
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
}