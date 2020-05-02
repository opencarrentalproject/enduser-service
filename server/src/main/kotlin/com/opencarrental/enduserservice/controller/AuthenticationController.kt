package com.opencarrental.enduserservice.controller

import com.opencarrental.enduserservice.domain.User
import com.opencarrental.enduserservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthenticationController(val service: UserService) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody user: User) {
        val result = service.create(user)
        // TODO delivery link to login
    }
}