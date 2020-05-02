package com.opencarrental.enduserservice.service

import com.opencarrental.enduserservice.api.UserEdit
import com.opencarrental.enduserservice.exception.EndUserNotFoundException
import com.opencarrental.enduserservice.exception.ErrorDetail
import com.opencarrental.enduserservice.exception.InvalidEndUserException
import com.opencarrental.enduserservice.exception.NotUniqueUserException
import com.opencarrental.enduserservice.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*


@Service("customUserDetailService")
class UserServiceImpl(val repository: UserRepository, val validationService: UserValidationService) : UserService {

    override fun create(user: com.opencarrental.enduserservice.domain.User): com.opencarrental.enduserservice.domain.User {
        validateEndUser(user)
        val validateUniqueUserError = validationService.validateUniqueEndUser(user).errors
        if (validateUniqueUserError.isNotEmpty()) {
            throw NotUniqueUserException(validateUniqueUserError.map {
                ErrorDetail(it.dataPath, it.message)
            })
        }
        return repository.save(user)
    }

    override fun retrieve(id: String): com.opencarrental.enduserservice.domain.User? {
        return repository.findByIdOrNull(id)
    }

    override fun update(id: String, userEdit: UserEdit): com.opencarrental.enduserservice.domain.User {
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

    override fun list(): List<com.opencarrental.enduserservice.domain.User> = repository.findAll()

    private fun validateEndUser(user: com.opencarrental.enduserservice.domain.User) {
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
