package com.opencarrental.authorizationservice.controller

import com.opencarrental.authorizationservice.api.RoleEdit
import com.opencarrental.authorizationservice.api.RoleResource
import com.opencarrental.authorizationservice.domain.Role
import com.opencarrental.authorizationservice.exception.RoleNotFoundException
import com.opencarrental.authorizationservice.service.RoleService
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/roles")
class RoleController(val service: RoleService) {

    @GetMapping(produces = ["application/hal+json"])
    fun listRoles(): CollectionModel<RoleResource> {
        val roles = service.list()
        val result = roles.stream()
                .map {
                    mapToResource(it)
                }.collect(Collectors.toList())
        val link: Link = WebMvcLinkBuilder.linkTo(RoleController::class.java).withSelfRel()
        return CollectionModel<RoleResource>(result, link)
    }

    @GetMapping("/{roleId}")
    fun retrieveUser(@PathVariable roleId: String) = service.retrieve(roleId)
            ?: throw RoleNotFoundException("Role does not exist")

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody role: Role): RoleResource {
        val result = service.create(role)
        return mapToResource(result)
    }

    @PatchMapping("/{roleId}")
    fun updateUser(@PathVariable roleId: String, @RequestBody roleEdit: RoleEdit): RoleResource {
        return mapToResource(service.update(roleId, roleEdit))
    }

    @DeleteMapping("/{roleId}")
    fun deleteUser(@PathVariable roleId: String) {
        service.delete(roleId)
    }

    private fun mapToResource(role: Role): RoleResource {
        val selfLink: Link = WebMvcLinkBuilder.linkTo(RoleController::class.java).slash(role.id).withSelfRel()
        val roleResource: RoleResource = RoleResource(
                id = role.id!!, name = role.name)

        return roleResource.add(selfLink)
    }
}