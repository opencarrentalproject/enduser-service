package com.opencarrental.authorizationservice.api

import com.opencarrental.authorizationservice.domain.Role
import org.springframework.hateoas.RepresentationModel
import java.time.LocalDateTime

class UserResource(val id: String, val firstName: String, val lastName: String,
                   val email: String, val verified: Boolean,
                   val registeredTime: LocalDateTime, val loggedInTime: LocalDateTime? = null, roles: Set<Role>) : RepresentationModel<UserResource>()