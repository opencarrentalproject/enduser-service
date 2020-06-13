package com.opencarrental.authorizationservice

import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserApiTest(@Autowired val dataProvider: DataProvider, @Autowired restTemplate: TestRestTemplate) : AbstractIntegrationTest(restTemplate) {

    val gson = Gson()

    @Test
    fun `request to create user must return success`() {

        val request = HttpEntity<CreateUserRequest>(CreateUserRequest("12345678",
                "first", "last", "example@example.com"))
        val response = testRestTemplate.postForEntity("/users", request, UserResource::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body?.id).isNotNull()
        assertThat(response.body?.firstName).isEqualTo("first")
        assertThat(response.body?._links).isNotNull()
    }

    @Test
    fun `request to create user with invalid password length`() {
        val request = HttpEntity<CreateUserRequest>(CreateUserRequest("12345",
                "first", "last", "example@example.com"))
        val response = testRestTemplate.postForEntity("/users", request, Array<ErrorDetail>::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body?.size).isEqualTo(1)
        assertThat(response.body?.get(0)?.field).isEqualTo(".password")
    }

    @Test
    fun `request to create user with invalid email`() {
        val request = HttpEntity<CreateUserRequest>(CreateUserRequest("12345678",
                "first", "last", "example.com"))
        val response = testRestTemplate.postForEntity("/users", request, Array<ErrorDetail>::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body?.size).isEqualTo(1)
        assertThat(response.body?.get(0)?.field).isEqualTo(".email")
    }

    @Test
    fun `request to create user with missing email and password`() {
        val request = HttpEntity<CreateUserRequest>(CreateUserRequest("",
                "first", "last", ""))
        val response = testRestTemplate.postForEntity("/users", request, Array<ErrorDetail>::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(response.body?.size).isEqualTo(2)
        assertThat(response.body).extracting("field").contains(".email", ".password")
    }

    @Test
    fun `request to create user with existing email`() {

        dataProvider.createUser("testUser1", "tester", "testUser1@example.com", "12345678")
        val request = HttpEntity<CreateUserRequest>(CreateUserRequest("abcdefgh",
                "first", "last", "testUser1@example.com"))
        val response = testRestTemplate.postForEntity("/users", request, Array<ErrorDetail>::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.CONFLICT)
        assertThat(response.body?.size).isEqualTo(1)
        assertThat(response.body?.get(0)?.message).isEqualTo("email must be unique")
    }

    @Test
    fun `request to list users must return list of users`() {

        dataProvider.createUsers()
        val response = testRestTemplate
                .getForEntity("/users", EndUserListResource::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body?._embedded?.userResourceList).extracting("firstName").contains("test1", "test2", "test3")
    }

    @Test
    fun `request to retrieve on user must return the correct user `() {
        val expectedUser = dataProvider.createUser("testUser1", "tester", "testUser1@example.com", "12345")

        val response = testRestTemplate.getForEntity("/users/${expectedUser.id}", UserResource::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).extracting("firstName").isEqualTo("testUser1")
        assertThat(response.body).extracting("email").isEqualTo("testUser1@example.com")
    }

    @Test
    fun `request to update first-,  last name and email must return updated resource`() {
        val persistedUser = dataProvider.createUser("testUser1", "tester", "testUser1@example.com", "12345678")

        val request = HttpEntity<String>(gson.toJson(UpdateUserRequest(
                firstName = "first", lastName = "last", email = "test.User1@example.com")))
        val response = testRestTemplate.patchForObject("/users/${persistedUser.id}", request, UserResource::class.java)

        assertThat(response).extracting("firstName").isEqualTo("first")
        assertThat(response).extracting("lastName").isEqualTo("last")
        assertThat(response).extracting("email").isEqualTo("test.User1@example.com")
        assertThat(response).extracting("_links").isNotNull()
    }

    @Test
    fun `request to update a non existing user must return not found`() {
        val request = HttpEntity<String>(gson.toJson(UpdateUserRequest(
                firstName = "first", lastName = "last", email = "test.User1@example.com")))
        val response = testRestTemplate.exchange("/users/1234", HttpMethod.PATCH, request, String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo("User does not exist")
    }

    @Test
    fun `request to delete a resource must return success`() {
        val persistedUser = dataProvider.createUser("testUser1", "tester", "testUser1@example.com", "12345")

        testRestTemplate.delete("/users/${persistedUser.id}")

        val response = testRestTemplate.getForEntity("/users/${persistedUser.id}", String::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)

    }

    @Test
    fun `request to delete a non exiting user must return not found`() {
        val response = testRestTemplate.exchange("/users/1234", HttpMethod.DELETE, null, String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).isEqualTo("User does not exist")
    }

    internal data class CreateUserRequest(
            val password: String,
            val firstName: String,
            val lastName: String,
            val email: String
    )

    internal data class UpdateUserRequest(
            val firstName: String,
            val lastName: String,
            val email: String
    )

    internal data class EndUserListResource(
            val _embedded: EmbeddedList,
            val _links: Links
    )

    internal data class EmbeddedList(
            val userResourceList: Array<UserResource>
    )

    internal data class UserResource(
            val id: String,
            val firstName: String,
            val lastName: String,
            val email: String,
            val verified: Boolean = false,
            val registeredTime: LocalDateTime,
            val loggedInTime: LocalDateTime? = null,
            val _links: Links? = null
    )

    internal data class ErrorDetail(
            val field: String,
            val message: String
    )
}


