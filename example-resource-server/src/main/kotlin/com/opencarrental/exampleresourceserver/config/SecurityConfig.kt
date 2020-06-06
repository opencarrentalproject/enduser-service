package com.opencarrental.exampleresourceserver.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler

@Configuration
@EnableResourceServer
class SecurityConfig() : ResourceServerConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.requestMatchers().antMatchers("/items/**")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/items/**").hasAuthority("admin")
                .antMatchers(HttpMethod.GET, "/items/**").access("#oauth2.hasScope('read') and hasAuthority('member')")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(OAuth2AccessDeniedHandler());
    }
}