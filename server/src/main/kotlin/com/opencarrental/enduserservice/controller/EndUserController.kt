package com.opencarrental.enduserservice.controller

import com.opencarrental.enduserservice.api.EndUserEdit
import com.opencarrental.enduserservice.api.EndUserResource
import com.opencarrental.enduserservice.domain.EndUser
import com.opencarrental.enduserservice.service.EndUserService
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.stream.Collectors

@RestController
@RequestMapping("/users")
class EndUserController(val service: EndUserService) {

    @GetMapping("", produces = ["application/hal+json"])
    fun listUsers(): CollectionModel<EndUserResource> {
        val users = service.list()
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
    fun retrieveUser(@PathVariable userId: String) = service.retrieve(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exists")

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody endUser: EndUser): EndUserResource {
        val result = service.create(endUser)
        return mapToResource(result)
    }

    @PatchMapping("/{userId}")
    fun updateUser(@PathVariable userId: String, @RequestBody endUserEdit: EndUserEdit): EndUserResource {
        return mapToResource(service.update(userId, endUserEdit))
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: String) {
        service.delete(userId)
    }

    private fun mapToResource(endUser: EndUser): EndUserResource {
        val endUserResource: EndUserResource = EndUserResource(
                id = endUser.id!!, firstName = endUser.firstName, lastName = endUser.lastName, email = endUser.email,
                registeredTime = endUser.registeredTime, loggedInTime = endUser.loggedInTime, verified = endUser.verified)
        val selfLink: Link = linkTo(EndUserController::class.java).slash(endUserResource.id).withSelfRel()
        endUserResource.add(selfLink)
        return endUserResource
    }
}