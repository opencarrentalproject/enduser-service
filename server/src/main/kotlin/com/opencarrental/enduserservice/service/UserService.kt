package com.opencarrental.enduserservice.service

import com.opencarrental.enduserservice.api.UserEdit
import com.opencarrental.enduserservice.domain.User
import org.springframework.security.core.userdetails.UserDetailsService


interface UserService : UserDetailsService {

    fun create(user: User): User

    fun retrieve(id: String): User?

    fun update(id: String, userEdit: UserEdit): User

    fun delete(id: String)

    fun list(): List<User>
}