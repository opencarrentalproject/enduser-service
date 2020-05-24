package com.opencarrental.authorizationservice.controller

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@CrossOrigin(origins = ["*"], maxAge = 3600)
@RestController
@RequestMapping("/auth")
class UserAdminAuthController(@Qualifier("adminAuthenticationManager") val authenticationManager: AuthenticationManager) {

}