package com.opencarrental.authorizationservice.configuration

import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@EnableWebSecurity(debug = true)
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http!!.cors()
                    .and()
                .authorizeRequests()
                    .antMatchers("/login", "/.well-known/jwks.json", "/authorizationCode", "/oauth/authorize")
                    .permitAll()
                    .and()
                .authorizeRequests()
                    .antMatchers("/users").hasAnyRole("ADMIN")
                    .anyRequest()
                    .authenticated()
                .and()
                    .formLogin()
                    .permitAll()
                    .and()
                .logout()
                    .permitAll()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }
}