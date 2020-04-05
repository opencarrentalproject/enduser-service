package com.opencarrental.enduserservice.domain

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class EndUser(
        @Id
        val id: String?,
        private val password: String,
        val firstName: String?,
        val lastName: String?,
        val email: String,
        val verified: Boolean? = false,
        val registeredTime: LocalDateTime? = LocalDateTime.now(),
        val loggedInTime: LocalDateTime?
)