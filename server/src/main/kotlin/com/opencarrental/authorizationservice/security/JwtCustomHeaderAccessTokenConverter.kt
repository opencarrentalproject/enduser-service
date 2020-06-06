package com.opencarrental.authorizationservice.security

import org.springframework.security.jwt.JwtHelper
import org.springframework.security.jwt.crypto.sign.RsaSigner
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.util.JsonParser
import org.springframework.security.oauth2.common.util.JsonParserFactory
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import java.security.KeyPair
import java.security.interfaces.RSAPrivateKey


class JwtCustomHeadersAccessTokenConverter(private val customHeaders: Map<String, String>, keyPair: KeyPair) : JwtAccessTokenConverter() {

    private val objectMapper: JsonParser = JsonParserFactory.create()
    val signer: RsaSigner
    override fun encode(accessToken: OAuth2AccessToken?, authentication: OAuth2Authentication?): String {
        val content: String
        content = try {
            this.objectMapper.formatMap(accessTokenConverter.convertAccessToken(accessToken, authentication))
        } catch (ex: Exception) {
            throw IllegalStateException("Cannot convert access token to JSON", ex)
        }
        return JwtHelper.encode(content, signer, customHeaders).encoded
    }

    init {
        super.setKeyPair(keyPair)
        this.signer = RsaSigner(keyPair.private as RSAPrivateKey)
    }
}