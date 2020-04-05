package com.opencarrental.enduserservice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EndUserserviceApplicationTests(@Autowired val restTemplate: TestRestTemplate, @Autowired val dataProvider: DataProvider) : AbstractIntegrationTest() {

    @Test
    fun `call create user must return success`() {

        val request = HttpEntity<CreateUserRequest>(CreateUserRequest("12345",
                "first", "last", "example@example.com"))
        val response = restTemplate.postForEntity("/users", request, EndUserResource::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.id).isNotNull()
        assertThat(response.body?.firstName).isEqualTo("first")
    }

    @Test
    fun `call getUsers must return list of users`() {

        dataProvider.createUsers()
        val response = restTemplate.getForEntity("/users", Array<EndUserResource>::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).extracting("firstName").contains("test1", "test2", "test3")
    }

    internal data class CreateUserRequest(
            val password: String,
            val firstName: String,
            val lastName: String,
            val email: String
    )

    internal data class EndUserResource(
            val id: String,
            val firstName: String,
            val lastName: String,
            val email: String,
            val verified: Boolean = false,
            val registeredTime: LocalDateTime,
            val loggedInTime: LocalDateTime? = null
    )
}


