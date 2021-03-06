package com.opencarrental.authorizationservice

import com.opencarrental.authorizationservice.domain.Role
import com.opencarrental.authorizationservice.domain.User
import com.opencarrental.authorizationservice.repository.RoleRepository
import com.opencarrental.authorizationservice.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional
class DataProvider(@Autowired val userRepository: UserRepository, @Autowired val roleRepository: RoleRepository) {

    fun createUsers() {
        val role = createRole("customer")
        userRepository.save(User(password = "123456", firstName = "test1", lastName = "test2", email = "test1@example.com", roles = setOf(role)))
        userRepository.save(User(password = "123456", firstName = "test2", lastName = "test2", email = "test2@example.com", roles = setOf(role)))
        userRepository.save(User(password = "123456", firstName = "test3", lastName = "test3", email = "test3@example.com", roles = setOf(role)))
    }

    fun createUser(firstName: String, lastName: String, email: String, password: String): User {
        val role = createRole("customer")
        return userRepository.save(User(firstName = firstName, lastName = lastName, email = email, password = password, roles = setOf(role)))
    }


    fun createRole(name: String) = roleRepository.save(Role(name = name))
}