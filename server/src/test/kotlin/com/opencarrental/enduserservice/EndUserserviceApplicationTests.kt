package com.opencarrental.enduserservice

import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EndUserserviceApplicationTests(@Autowired val restTemplate: TestRestTemplate, @Autowired val dataProvider: DataProvider) : AbstractIntegrationTest() {

    val gson = Gson()
    val headers = HttpHeaders()

    @BeforeEach
    fun setup() {
        val map = mapOf(
                "Content-Type" to listOf<String>("application/json")
        )
        headers.putAll(map)
    }

    @Test
    fun `request to create user must return success`() {

        val request = HttpEntity<CreateUserRequest>(CreateUserRequest("12345",
                "first", "last", "example@example.com"))
        val response = restTemplate.postForEntity("/users", request, EndUserResource::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.id).isNotNull()
        assertThat(response.body?.firstName).isEqualTo("first")
    }

    @Test
    fun `request to list users must return list of users`() {

        dataProvider.createUsers()
        val response = restTemplate.getForEntity("/users", Array<EndUserResource>::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).extracting("firstName").contains("test1", "test2", "test3")
    }

    @Test
    fun `request to retrieve on user must return the correct user `() {
        val expectedUser = dataProvider.createUser("testUser1", "tester", "testUser1@example.com", "12345")

        val response = restTemplate.getForEntity("/users/${expectedUser.id}", EndUserResource::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).extracting("firstName").isEqualTo("testUser1")
        assertThat(response.body).extracting("email").isEqualTo("testUser1@example.com")
    }

    @Test
    fun `request to update first and last name must return updated resource`() {
        val persistedUser = dataProvider.createUser("testUser1", "tester", "testUser1@example.com", "12345")

        val request = HttpEntity<String>(gson.toJson(UpdateUserRequest(
                firstName = "first", lastName = "last")), headers)
        val response = restTemplate.patchForObject("/users/${persistedUser.id}", request, EndUserResource::class.java)

        assertThat(response).extracting("firstName").isEqualTo("first")
        assertThat(response).extracting("lastName").isEqualTo("last")
        assertThat(response).extracting("email").isEqualTo("testUser1@example.com")
    }

    @Test
    fun `request to delete a resource must return success`() {
        val persistedUser = dataProvider.createUser("testUser1", "tester", "testUser1@example.com", "12345")

        restTemplate
                .delete("/users/${persistedUser.id}")

        val response = restTemplate.getForEntity("/users/${persistedUser.id}", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)

    }

    internal data class CreateUserRequest(
            val password: String,
            val firstName: String,
            val lastName: String,
            val email: String
    )

    internal data class UpdateUserRequest(
            val firstName: String,
            val lastName: String
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


