package com.opencarrental.authorizationservice.controller

import com.nimbusds.jose.jwk.JWKSet
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class JwkSetController(val jwkSet: JWKSet) {

    @GetMapping("/.well-known/jwks.json")
    fun keys(): Map<String, Any> = jwkSet.toJSONObject().toMap()
}