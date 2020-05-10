package com.opencarrental.authorizationservice.controller

import com.opencarrental.authorizationservice.domain.User
import com.opencarrental.authorizationservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthenticationController(val service: UserService) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody user: User) = service.create(user)
}