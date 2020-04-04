package com.opencarrental.enduserservice.controller

import com.opencarrental.enduserservice.api.EndUserEdit
import com.opencarrental.enduserservice.domain.EndUser
import com.opencarrental.enduserservice.repository.EndUserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/users")
class EndUserController(val repository: EndUserRepository) {

    @GetMapping("")
    fun listUsers() = repository.findAllOrderByLogin()

    @GetMapping("/{userId}")
    fun retrieveUser(@PathVariable userId: UUID) = repository.findById(userId)

    @PostMapping("")
    fun createUser(@RequestBody endUser: EndUser) = repository.save(endUser)

    @PatchMapping("/{userId}")
    fun updateUser(@PathVariable userId: UUID, @RequestBody endEndUserEdit: EndUserEdit) {
        val persistedUser = repository.findByIdOrNull(userId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists")
        val updatedUser = persistedUser.copy(firstName = endEndUserEdit.firstName
                ?: persistedUser.firstName, lastName = endEndUserEdit.lastName
                ?: persistedUser.lastName, email = endEndUserEdit.email ?: persistedUser.email)
        repository.save(updatedUser)
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: UUID) {
        val persistedUser = repository.findByIdOrNull(userId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists")

        repository.delete(persistedUser)
    }
}