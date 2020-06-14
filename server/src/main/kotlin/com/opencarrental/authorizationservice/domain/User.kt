package com.opencarrental.authorizationservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class User(
        @Id
        val id: String? = null,
        val password: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val verified: Boolean = false,
        val registeredTime: LocalDateTime = LocalDateTime.now(),
        val loggedInTime: LocalDateTime? = null,
        @DBRef val roles: Set<Role> = setOf()
)