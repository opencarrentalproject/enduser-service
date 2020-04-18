package com.opencarrental.enduserservice.service

import com.opencarrental.enduserservice.api.EndUserEdit
import com.opencarrental.enduserservice.domain.EndUser
import com.opencarrental.enduserservice.repository.EndUserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class EndUserServiceImpl(val repository: EndUserRepository) : EndUserService {

    override fun create(endUser: EndUser): EndUser {
        return repository.save(endUser)
    }

    override fun retrieve(id: String): EndUser? {
        return repository.findByIdOrNull(id)
    }

    override fun update(id: String, endUserEdit: EndUserEdit): EndUser {
        val persistedUser = repository.findByIdOrNull(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist") // TODO throw dedicated exception
        val updatedUser = persistedUser.copy(firstName = endUserEdit.firstName
                ?: persistedUser.firstName, lastName = endUserEdit.lastName
                ?: persistedUser.lastName, email = endUserEdit.email ?: persistedUser.email)
        return repository.save(updatedUser)
    }

    override fun delete(id: String) {
        val persistedUser = repository.findByIdOrNull(id)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist") // TODO throw dedicated exception

        repository.delete(persistedUser)
    }

    override fun list(): List<EndUser> = repository.findAll()
}