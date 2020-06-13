package com.opencarrental.authorizationservice

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoleApiTest(@Autowired val dataProvider: DataProvider, @Autowired restTemplate: TestRestTemplate) : AbstractIntegrationTest(restTemplate) {

    @Test
    fun `request to create a role must return success`() {

        val request = HttpEntity<CreateUpdateRoleRequest>(CreateUpdateRoleRequest("member"))
        val response = testRestTemplate.postForEntity("/roles", request, RoleResource::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        Assertions.assertThat(response.body?.id).isNotNull()
        Assertions.assertThat(response.body?.name).isEqualTo("member")
    }

    @Test
    fun `request to list roles must return success`() {
        dataProvider.createRole("customer")
        dataProvider.createRole("admin")
        dataProvider.createRole("member")

        val response = testRestTemplate
                .getForEntity("/roles", RoleListResource::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body?._embedded?.roleResourceList).extracting("name").contains("customer", "admin", "member")

    }

    @Test
    fun `request to retrieve a role must return success`() {
        val expectedRole = dataProvider.createRole("customer")

        val response = testRestTemplate.getForEntity("/roles/${expectedRole.id}", RoleResource::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        Assertions.assertThat(response.body?.id).isEqualTo(expectedRole.id)
        Assertions.assertThat(response.body?.name).isEqualTo("customer")
    }

    @Test
    fun `request to update a role must return success`() {
        val persistedRole = dataProvider.createRole("customer")
        val response = testRestTemplate.patchForObject("/roles/${persistedRole.id}", CreateUpdateRoleRequest(name = "customerUpdate"), RoleResource::class.java)

        Assertions.assertThat(response).extracting("name").isEqualTo("customerUpdate")
    }

    @Test
    fun `request to remove a role must return success`() {
        val persistedRole = dataProvider.createRole("customer")

        testRestTemplate.delete("/roles/${persistedRole.id}")

        val response = testRestTemplate.getForEntity("/roles/${persistedRole.id}", String::class.java)

        Assertions.assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }

    internal data class CreateUpdateRoleRequest(
            val name: String
    )

    internal data class RoleResource(
            val id: String,
            val name: String,
            val _links: Links? = null
    )

    internal data class EmbeddedList(
            val roleResourceList: Array<RoleResource>
    )

    internal data class RoleListResource(
            val _embedded: EmbeddedList,
            val _links: Links
    )
}