package com.opencarrental.authorizationservice.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC512
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.opencarrental.authorizationservice.api.UserLogin
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthenticationFilter(private val adminAuthenticationManager: AuthenticationManager,
                              private val jwtSecret: String,
                              private val jwtTokenValidity: Long, filterUrl: String) : UsernamePasswordAuthenticationFilter() {

    init {
        this.setFilterProcessesUrl(filterUrl)
    }

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val creds: UserLogin = jacksonObjectMapper()
                .readValue(request!!.inputStream, UserLogin::class.java)
        return adminAuthenticationManager.authenticate(UsernamePasswordAuthenticationToken(
                creds.username, creds.password, emptyList()
        ))
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {
        val token: String = JWT.create()
                .withSubject((authResult!!.principal as User).username)
                .withExpiresAt(Date(System.currentTimeMillis() + jwtTokenValidity))
                .sign(HMAC512(jwtSecret))
        val writer = response!!.writer
        writer.write(token)
        writer.flush()
    }
}