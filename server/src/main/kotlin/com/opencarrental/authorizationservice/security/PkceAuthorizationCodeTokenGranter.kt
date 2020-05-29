package com.opencarrental.authorizationservice.security

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.common.exceptions.InvalidClientException
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException
import org.springframework.security.oauth2.provider.*
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices
import java.util.*

class PkceAuthorizationCodeTokenGranter(tokenServices: AuthorizationServerTokenServices?, private val authorizationCodeServices: PkceAuthorizationCodeServices, clientDetailsService: ClientDetailsService?, requestFactory: OAuth2RequestFactory?)
    : AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory) {

    override fun getOAuth2Authentication(client: ClientDetails, tokenRequest: TokenRequest): OAuth2Authentication {

        val parameters = tokenRequest.requestParameters
        val authorizationCode = parameters["code"]
        val redirectUri = parameters["redirect_uri"]
        return if (authorizationCode == null) {
            throw InvalidRequestException("An authorization code must be supplied.")
        } else {
            val codeVerifier = parameters.getOrDefault("code_verifier", "")
            val storedAuth = authorizationCodeServices.consumeAuthorizationCodeAndCodeVerifier(authorizationCode, codeVerifier)
            if (storedAuth == null) {
                throw InvalidGrantException("Invalid authorization code: $authorizationCode")
            } else {
                val pendingOAuth2Request = storedAuth.oAuth2Request
                val redirectUriApprovalParameter = pendingOAuth2Request.requestParameters["redirect_uri"]
                if ((redirectUri != null || redirectUriApprovalParameter != null) && pendingOAuth2Request.redirectUri != redirectUri) {
                    throw RedirectMismatchException("Redirect URI mismatch.")
                } else {
                    val pendingClientId = pendingOAuth2Request.clientId
                    val clientId = tokenRequest.clientId
                    if (clientId != null && clientId != pendingClientId) {
                        throw InvalidClientException("Client ID mismatch")
                    } else {
                        val combinedParameters: MutableMap<String, String> = HashMap(pendingOAuth2Request.requestParameters)
                        combinedParameters.putAll(parameters)
                        val finalStoredOAuth2Request = pendingOAuth2Request.createOAuth2Request(combinedParameters)
                        val userAuth: Authentication = storedAuth.userAuthentication
                        OAuth2Authentication(finalStoredOAuth2Request, userAuth)
                    }
                }
            }
        }
    }

}