package com.opencarrental.authorizationservice.service

import com.opencarrental.authorizationservice.api.UserEdit
import com.opencarrental.authorizationservice.domain.User
import org.springframework.security.core.userdetails.UserDetailsService


interface UserService : UserDetailsService {

    fun create(user: User): User

    fun retrieve(id: String): User?

    fun update(id: String, userEdit: UserEdit): User

    fun delete(id: String)

    fun list(): List<User>
}