package com.opencarrental.authorizationservice.controller

import com.opencarrental.authorizationservice.domain.User
import com.opencarrental.authorizationservice.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus


@Controller
class AuthenticationController(val service: UserService) {

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody user: User) = service.create(user)

    @GetMapping("/login")
    fun login(): String? {
        return "login"
    }

    @GetMapping("/authorizationCode")
    fun authorizationCode(): String? {
        return "authorizationCode"
    }

    @GetMapping("/login-error")
    fun loginError(model: Model): String? {
        print("login error")
        model.addAttribute("loginError", true)
        return login()
    }
}