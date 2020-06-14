package com.opencarrental.authorizationservice.api

import com.opencarrental.authorizationservice.domain.Role

data class UserEdit(
        val firstName: String?,
        val lastName: String?,
        val email: String?,
        val roles: Set<Role>
)