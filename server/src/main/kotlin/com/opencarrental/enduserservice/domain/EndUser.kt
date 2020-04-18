package com.opencarrental.enduserservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class EndUser(
        @Id
        val id: String? = null,
        val password: String,
        val firstName: String,
        val lastName: String,
        val email: String,
        val verified: Boolean = false,
        val registeredTime: LocalDateTime = LocalDateTime.now(),
        val loggedInTime: LocalDateTime? = null
)