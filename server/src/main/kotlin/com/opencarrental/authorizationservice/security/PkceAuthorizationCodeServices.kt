package com.opencarrental.authorizationservice.security

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices
import java.util.concurrent.ConcurrentHashMap

class PkceAuthorizationCodeServices(private val clientDetailsService: ClientDetailsService, private val passwordEncoder: PasswordEncoder) : AuthorizationCodeServices {

    private val generator = RandomValueStringGenerator()
    private val authorizationCodeStore: MutableMap<String, PkceProtectedAuthentication> = ConcurrentHashMap()
    override fun createAuthorizationCode(authentication: OAuth2Authentication): String {
        val protectedAuthentication = getProtectedAuthentication(authentication)
        val code = generator.generate()
        authorizationCodeStore[code] = protectedAuthentication
        return code
    }

    private fun getProtectedAuthentication(authentication: OAuth2Authentication): PkceProtectedAuthentication {
        val requestParameters = authentication.oAuth2Request.requestParameters
        if (isPublicClient(requestParameters["client_id"]) && !requestParameters.containsKey("code_challenge")) {
            throw InvalidRequestException("Code challenge required.")
        }
        if (requestParameters.containsKey("code_challenge")) {
            val codeChallenge = requestParameters["code_challenge"]
            val codeChallengeMethod = getCodeChallengeMethod(requestParameters)
            return PkceProtectedAuthentication(codeChallenge, codeChallengeMethod, authentication)
        }
        return PkceProtectedAuthentication(authentication)
    }

    private fun getCodeChallengeMethod(requestParameters: Map<String, String>): CodeChallengeMethod {
        val codeChallengeMethodVal = requestParameters["code_challenge_method"]
        return if (codeChallengeMethodVal == null) CodeChallengeMethod.NONE else CodeChallengeMethod.valueOf(codeChallengeMethodVal)
    }


    private fun isPublicClient(clientId: String?): Boolean {
        val clientSecret = clientDetailsService.loadClientByClientId(clientId).clientSecret
        return clientSecret == null || passwordEncoder.matches("", clientSecret)
    }

    override fun consumeAuthorizationCode(code: String): OAuth2Authentication {
        throw UnsupportedOperationException()
    }

    fun consumeAuthorizationCodeAndCodeVerifier(code: String?, verifier: String?): OAuth2Authentication? {
        return authorizationCodeStore[code]!!.getAuthentication(verifier)
    }

}