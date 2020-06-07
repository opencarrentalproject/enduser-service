package com.opencarrental.authorizationservice.domain

import org.springframework.data.annotation.Id

data class Role(
        @Id
        val id: String? = null,
        val name: String
)