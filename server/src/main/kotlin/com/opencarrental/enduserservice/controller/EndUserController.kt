package com.opencarrental.enduserservice.controller

import com.opencarrental.enduserservice.api.EndUserEdit
import com.opencarrental.enduserservice.api.EndUserResource
import com.opencarrental.enduserservice.domain.EndUser
import com.opencarrental.enduserservice.repository.EndUserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.stream.Collectors

@RestController
@RequestMapping("/users")
class EndUserController(val repository: EndUserRepository) {

    @GetMapping("", produces = ["application/hal+json"])
    fun listUsers(): CollectionModel<EndUserResource> {
        val users = repository.findAll()
        val result = users.stream()
                .map {

                    val selfLink: Link = linkTo(EndUserController::class.java).slash(it.id).withSelfRel()
                    val endUserResource: EndUserResource = EndUserResource(
                            id = it.id!!, firstName = it.firstName, lastName = it.lastName, email = it.email,
                            registeredTime = it.registeredTime, loggedInTime = it.loggedInTime, verified = it.verified)

                    endUserResource.add(selfLink)
                }.collect(Collectors.toList())
        val link: Link = linkTo(EndUserController::class.java).withSelfRel()
        return CollectionModel<EndUserResource>(result, link)
    }


    @GetMapping("/{userId}")
    fun retrieveUser(@PathVariable userId: String) = repository.findByIdOrNull(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists")

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody endUser: EndUser) = repository.save(endUser)

    @PatchMapping("/{userId}")
    fun updateUser(@PathVariable userId: String, @RequestBody endEndUserEdit: EndUserEdit): EndUser {
        val persistedUser = repository.findByIdOrNull(userId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists")
        val updatedUser = persistedUser.copy(firstName = endEndUserEdit.firstName
                ?: persistedUser.firstName, lastName = endEndUserEdit.lastName
                ?: persistedUser.lastName, email = endEndUserEdit.email ?: persistedUser.email)
        return repository.save(updatedUser)
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: String) {
        val persistedUser = repository.findByIdOrNull(userId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists")

        repository.delete(persistedUser)
    }
}