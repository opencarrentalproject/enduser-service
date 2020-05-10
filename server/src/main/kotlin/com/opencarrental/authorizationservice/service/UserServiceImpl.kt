package com.opencarrental.authorizationservice.service

import com.opencarrental.authorizationservice.api.UserEdit
import com.opencarrental.authorizationservice.exception.EndUserNotFoundException
import com.opencarrental.authorizationservice.exception.ErrorDetail
import com.opencarrental.authorizationservice.exception.InvalidEndUserException
import com.opencarrental.authorizationservice.exception.NotUniqueUserException
import com.opencarrental.authorizationservice.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service("customUserDetailService")
class UserServiceImpl(val repository: UserRepository, val validationService: UserValidationService, val passwordEncoder: PasswordEncoder) : UserService {

    override fun create(user: com.opencarrental.authorizationservice.domain.User): com.opencarrental.authorizationservice.domain.User {
        validateEndUser(user)
        val validateUniqueUserError = validationService.validateUniqueEndUser(user).errors
        if (validateUniqueUserError.isNotEmpty()) {
            throw NotUniqueUserException(validateUniqueUserError.map {
                ErrorDetail(it.dataPath, it.message)
            })
        }
        val userWithEncryptedPassword = user.copy(password = passwordEncoder.encode(user.password))
        return repository.save(userWithEncryptedPassword)
    }

    override fun retrieve(id: String): com.opencarrental.authorizationservice.domain.User? {
        return repository.findByIdOrNull(id)
    }

    override fun update(id: String, userEdit: UserEdit): com.opencarrental.authorizationservice.domain.User {
        val persistedUser = repository.findByIdOrNull(id)
                ?: throw EndUserNotFoundException("User does not exist")
        val updatedUser = persistedUser.copy(firstName = userEdit.firstName
                ?: persistedUser.firstName, lastName = userEdit.lastName
                ?: persistedUser.lastName, email = userEdit.email ?: persistedUser.email)
        validateEndUser(updatedUser)
        return repository.save(updatedUser)
    }

    override fun delete(id: String) {
        val persistedUser = repository.findByIdOrNull(id)
                ?: throw EndUserNotFoundException("User does not exist")

        repository.delete(persistedUser)
    }

    override fun list(): List<com.opencarrental.authorizationservice.domain.User> = repository.findAll()

    private fun validateEndUser(user: com.opencarrental.authorizationservice.domain.User) {
        val validationErrors = validationService.validateUser(user).errors
        if (validationErrors.isNotEmpty()) {
            throw InvalidEndUserException(validationErrors.map {
                ErrorDetail(it.dataPath, it.message)
            })
        }
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        val email = username ?: throw IllegalArgumentException("Username not supplieds")
        val endUser = repository.findFirstByEmail(email) ?: throw RuntimeException("User not found")

        // TODO apply authority after implementing roles
        return User(endUser.email, endUser.password, Collections.emptyList())
    }

}
