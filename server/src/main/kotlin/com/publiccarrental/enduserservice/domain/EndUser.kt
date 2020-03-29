package com.publiccarrental.enduserservice.domain

import org.springframework.data.annotation.Id
import java.time.LocalDateTime
import java.util.*

data class EndUser(
        @Id
        val id: UUID?,
        val loginName: String,
        private val password: String,
        val firstName: String?,
        val lastName: String?,
        val email: String,
        val verified: Boolean?=false,
        val registeredTime: LocalDateTime,
        val loggedInTime: LocalDateTime
        )