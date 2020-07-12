package com.opencarrental.authorizationservice.configuration


import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import com.opencarrental.authorizationservice.security.JwtCustomHeadersAccessTokenConverter
import com.opencarrental.authorizationservice.security.PkceAuthorizationCodeServices
import com.opencarrental.authorizationservice.security.PkceAuthorizationCodeTokenGranter
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.CompositeTokenGranter
import org.springframework.security.oauth2.provider.OAuth2RequestFactory
import org.springframework.security.oauth2.provider.TokenGranter
import org.springframework.security.oauth2.provider.approval.ApprovalStore
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import java.security.KeyPair
import java.security.interfaces.RSAPublicKey

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration(val passwordEncoder: PasswordEncoder,
                                       @Qualifier("clientAuthenticationManager") val authenticationManager: AuthenticationManager,
                                       val clientDetailsService: ClientDetailsService,
                                       @Value("\${public_client.client_id}") val public: String,
                                       @Value("\${public_client.access_token_validity_period}") val accessTokenValidity: Int,
                                       @Value("\${public_client.refresh_token_validity_period}") val refreshTokenValidity: Int,
                                       @Value("\${public_client.redirect_urls}") val redirectUrls: String,
                                       @Value("\${private_client.client_id}") val private: String,
                                       @Value("\${private_client.client_secret}") val privateClientSecret: String) : AuthorizationServerConfigurerAdapter() {

    private val JWK_KID = "auth-key-id"

    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security!!.allowFormAuthenticationForClients()
    }


    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients!!.inMemory()
                .withClient(public) // Configuration for internet facing public client
                .secret("{noop}")
                .authorizedGrantTypes(AuthorizationGrantType.AUTHORIZATION_CODE.value,
                        AuthorizationGrantType.REFRESH_TOKEN.value)
                .redirectUris(redirectUrls)
                .autoApprove(true)
                .scopes("read")
                .accessTokenValiditySeconds(accessTokenValidity)
                .refreshTokenValiditySeconds(refreshTokenValidity)
                .and() // Configuration for internal service client
                .withClient(private)
                .secret(privateClientSecret)
                .authorizedGrantTypes(AuthorizationGrantType.CLIENT_CREDENTIALS.value,
                        AuthorizationGrantType.REFRESH_TOKEN.value)
                .accessTokenValiditySeconds(accessTokenValidity)
                .refreshTokenValiditySeconds(refreshTokenValidity)

    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints!!.tokenStore(tokenStore())
                .authorizationCodeServices(PkceAuthorizationCodeServices(endpoints.clientDetailsService, passwordEncoder))
                .tokenGranter(tokenGranter(endpoints))
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager);
    }

    @Bean
    fun keyPair(): KeyPair {
        val ksFile = ClassPathResource("auth-jwt.jks")
        val ksFactory = KeyStoreKeyFactory(ksFile, "auth-pass".toCharArray())
        return ksFactory.getKeyPair("auth-jwt")
    }

    @Bean
    fun jwtAccessTokenConverter(): JwtAccessTokenConverter {
        val customHeaders: Map<String, String> = mapOf(
                "kid" to JWK_KID
        )
        return JwtCustomHeadersAccessTokenConverter(customHeaders, keyPair())
    }

    @Bean
    fun tokenStore(): TokenStore = JwtTokenStore(jwtAccessTokenConverter())

    @Bean
    fun jwkSet(): JWKSet? {
        val publicKey = keyPair().public as RSAPublicKey
        val key: RSAKey = RSAKey
                .Builder(publicKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(JWK_KID).build()
        return JWKSet(key)
    }

    @Bean
    fun approvalStore(): ApprovalStore? {
        return InMemoryApprovalStore()
    }

    private fun tokenGranter(endpoints: AuthorizationServerEndpointsConfigurer): TokenGranter? {

        val tokenServices: AuthorizationServerTokenServices = endpoints.tokenServices
        val authorizationCodeServices: AuthorizationCodeServices = endpoints.authorizationCodeServices
        val clientDetailsService = endpoints.clientDetailsService
        val requestFactory: OAuth2RequestFactory = endpoints.oAuth2RequestFactory
        val granters: List<TokenGranter> = listOf(
                RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory),
                ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory),
                ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory),
                ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetailsService, requestFactory),
                PkceAuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices as PkceAuthorizationCodeServices, clientDetailsService, requestFactory)
        )
        return CompositeTokenGranter(granters)
    }
}