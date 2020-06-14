package com.opencarrental.authorizationservice.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm.HMAC512
import com.auth0.jwt.exceptions.JWTVerificationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTAuthorizationFilter(@Qualifier("adminAuthenticationManager") authManager: AuthenticationManager?,
                             private val jwtSecret: String) : BasicAuthenticationFilter(authManager) {

    val log: Logger = LoggerFactory.getLogger(JWTAuthorizationFilter::class.java)

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest,
                                  res: HttpServletResponse,
                                  chain: FilterChain) {
        val header = req.getHeader("Authorization")
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(req, res)
            return
        }
        try {
            val authentication = getAuthentication(req)
            SecurityContextHolder.getContext().authentication = authentication
        } catch (ex: JWTVerificationException) {
            log.warn("Jwt verification failed")
            // Setting authentication to null so that spring security can recognized this as authorized request
            SecurityContextHolder.getContext().authentication = null
        }
        chain.doFilter(req, res)
    }

    private fun getAuthentication(request: HttpServletRequest): UsernamePasswordAuthenticationToken? {
        val token = request.getHeader("Authorization")
        if (token != null) {
            // parse the token.
            val user: String = JWT.require(HMAC512(jwtSecret))
                    .build()
                    .verify(token.replace("Bearer ", ""))
                    .subject
            log.info("Parsing result $user")
            return UsernamePasswordAuthenticationToken(user, null, ArrayList())

        }
        return null
    }
}