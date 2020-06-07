package com.opencarrental.authorizationservice.configuration

import com.opencarrental.authorizationservice.security.JWTAuthenticationFilter
import com.opencarrental.authorizationservice.security.JWTAuthorizationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
@Order(1)
class UserApiSecurityConfiguration(@Autowired val passwordEncoder: PasswordEncoder,
                                   @Value("\${admin.username}") val adminUserName: String,
                                   @Value("\${admin.password}") val adminPassword: String,
                                   @Value("\${admin.jwt_secret}") val jwtSecret: String,
                                   @Value("\${admin.jwt_token_validity_period}") val jwtTokenValidity: Long) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {

        http!!.cors().configurationSource(corsConfigurationSource())
                .and()
                .csrf().disable()
                .requestMatchers()
                .antMatchers("/.well-known/jwks.json", "/admin/login", "/users/**", "/roles/**")
                .and()
                .authorizeRequests()
                .antMatchers("/.well-known/jwks.json", "/admin/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(JWTAuthenticationFilter(authenticationManagerBean(), jwtSecret, jwtTokenValidity, "/admin/login"))
                .addFilter(JWTAuthorizationFilter(authenticationManagerBean(), jwtSecret))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));

    }

    @Bean("adminAuthenticationManager")
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth!!.userDetailsService(adminUserDetailsService())
                .passwordEncoder(passwordEncoder)
    }


    @Bean
    fun adminUserDetailsService(): UserDetailsService? {
        val user: UserDetails = User
                .withUsername(adminUserName)
                .password(passwordEncoder.encode(adminPassword))
                .roles("ADMIN")
                .build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource? {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:8080")
        configuration.allowedMethods = listOf("GET", "POST", "PATCH", "DELETE", "OPTIONS")
        configuration.applyPermitDefaultValues();
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}