package com.opencarrental.authorizationservice.controller

import com.opencarrental.authorizationservice.api.UserEdit
import com.opencarrental.authorizationservice.api.UserResource
import com.opencarrental.authorizationservice.domain.User
import com.opencarrental.authorizationservice.exception.EndUserNotFoundException
import com.opencarrental.authorizationservice.service.UserService
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/users")
class UserController(val service: UserService) {

    @GetMapping("", produces = ["application/hal+json"])
    fun listUsers(): CollectionModel<UserResource> {
        val users = service.list()
        val result = users.stream()
                .map {

                    val selfLink: Link = linkTo(UserController::class.java).slash(it.id).withSelfRel()
                    val userResource: UserResource = UserResource(
                            id = it.id!!, firstName = it.firstName, lastName = it.lastName, email = it.email,
                            registeredTime = it.registeredTime, loggedInTime = it.loggedInTime, verified = it.verified)

                    userResource.add(selfLink)
                }.collect(Collectors.toList())
        val link: Link = linkTo(UserController::class.java).withSelfRel()
        return CollectionModel<UserResource>(result, link)
    }


    @GetMapping("/{userId}")
    fun retrieveUser(@PathVariable userId: String) = service.retrieve(userId)
            ?: throw EndUserNotFoundException("User does not exists")

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody user: User): UserResource {
        val result = service.create(user)
        return mapToResource(result)
    }

    @PatchMapping("/{userId}")
    fun updateUser(@PathVariable userId: String, @RequestBody userEdit: UserEdit): UserResource {
        return mapToResource(service.update(userId, userEdit))
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: String) {
        service.delete(userId)
    }

    private fun mapToResource(user: User): UserResource {
        val userResource: UserResource = UserResource(
                id = user.id!!, firstName = user.firstName, lastName = user.lastName, email = user.email,
                registeredTime = user.registeredTime, loggedInTime = user.loggedInTime, verified = user.verified)
        val selfLink: Link = linkTo(UserController::class.java).slash(userResource.id).withSelfRel()
        userResource.add(selfLink)
        return userResource
    }
}