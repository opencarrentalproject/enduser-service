package com.opencarrental.authorizationclient.controller

import org.springframework.http.HttpEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController("/auth")
class AuthController(val restTemplate: RestTemplate, val authServerUrl: String) {

    val headers = mapOf(
            "Authorization" to listOf<String>("Basic ${Base64.getEncoder().encodeToString("admin_client:admin".toByteArray())}"),
            "Content-Type" to listOf<String>(ContentType.APPLICATION_FORM_URLENCODED.mimeType)
    )
    @PostMapping("/signin")
    fun loginUser(@RequestBody loginCredentials: LoginCredentials): String {
        restTemplate.getForEntity<>()
    }

    val map = mapOf(

    )
    headers.putAll(map)
    val formParams: MultiValueMap<String, String> = LinkedMultiValueMap()
    formParams.add("grant_type", "client_credentials")
    val tokenRequest = HttpEntity<MultiValueMap<String, String>>(formParams, headers)
    val response = testRestTemplate.postForEntity("/oauth/token", tokenRequest, Token::class.java)
    token = response.body?.access_token
    ?: throw RuntimeException("Failed to retrieve token.Check the security configuration")
}