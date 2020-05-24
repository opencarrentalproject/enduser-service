package com.opencarrental.authorizationservice.configuration

import com.opencarrental.authorizationservice.security.JWTAuthenticationFilter
import com.opencarrental.authorizationservice.security.JWTAuthorizationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager


@Configuration
@EnableWebSecurity
class UserApiSecurityConfiguration(@Value("\${admin.username}") val adminUserName: String,
                                   @Value("\${admin.password}") val adminPassword: String,
                                   @Value("\${admin.jwt_secret}") val jwtSecret: String,
                                   @Value("\${admin.jwt_token_validity_period}") val jwtTokenValidity: Long) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http!!.cors()
                    .and().csrf().disable()
                        .authorizeRequests()
                        .antMatchers("/.well-known/jwks.json").permitAll()
                        .anyRequest().authenticated()
                     .and()
                        .addFilter(JWTAuthenticationFilter(authenticationManager(), jwtSecret, jwtTokenValidity))
                        .addFilter(JWTAuthorizationFilter(authenticationManager(), jwtSecret))
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean("adminAuthenticationManager")
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(adminUserDetailsService())
                .passwordEncoder(passwordEncoder())
    }


    @Bean
    fun adminUserDetailsService(): UserDetailsService? {
        val user: UserDetails = User
                .withUsername(adminUserName)
                .password(passwordEncoder().encode(adminPassword))
                .roles("ADMIN")
                .build()
        return InMemoryUserDetailsManager(user)
    }
}