package com.opencarrental.authorizationservice.configuration
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.approval.ApprovalStore
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler
import org.springframework.security.oauth2.provider.approval.InMemoryApprovalStore
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import java.security.KeyPair
import java.security.interfaces.RSAPublicKey

@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration(val passwordEncoder: PasswordEncoder,
                                       val authenticationManager: AuthenticationManager,
                                       val clientDetailsService: ClientDetailsService,
                                       @Qualifier("customUserDetailService") val userDetailsService: UserDetailsService,
                                       @Value("\${public_client.client_id}") val public: String,
                                       @Value("\${public_client.client_secret}") val publicSecret: String,
                                       @Value("\${public_client.access_token_validity_period}") val accessTokenValidity: Int,
                                       @Value("\${public_client.refresh_token_validity_period}") val refreshTokenValidity: Int) : AuthorizationServerConfigurerAdapter() {

    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security!!.allowFormAuthenticationForClients()
    }


    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients!!.inMemory()
                .withClient(public)
                .secret(passwordEncoder.encode(publicSecret))
                .redirectUris("http://localhost:8080/authorizationCode")
                .authorizedGrantTypes("authorization_code")
                .autoApprove(true)
                .scopes("read")
                .accessTokenValiditySeconds(accessTokenValidity)
                .refreshTokenValiditySeconds(refreshTokenValidity)
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints!!.tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .userApprovalHandler(userApprovalHandler())
                .userDetailsService(userDetailsService);
    }

    @Bean
    fun keyPair(): KeyPair {
        val ksFile = ClassPathResource("auth-jwt.jks")
        val ksFactory = KeyStoreKeyFactory(ksFile, "auth-pass".toCharArray())
        return ksFactory.getKeyPair("auth-jwt")
    }

    @Bean
    fun jwtAccessTokenConverter(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        converter.setKeyPair(keyPair())
        return converter
    }

    @Bean
    fun tokenStore(): TokenStore = JwtTokenStore(jwtAccessTokenConverter())

    @Bean
    fun jwkSet(): JWKSet? {
        val publicKey = keyPair().public as RSAPublicKey
        val key: RSAKey = RSAKey.Builder(publicKey).build()
        return JWKSet(key)
    }

    @Bean
    fun approvalStore(): ApprovalStore? {
        return InMemoryApprovalStore()
    }

    @Bean
    fun userApprovalHandler(): UserApprovalHandler? {
        val userApprovalHandler = ApprovalStoreUserApprovalHandler()
        userApprovalHandler.setApprovalStore(approvalStore())
        userApprovalHandler.setClientDetailsService(clientDetailsService)
        userApprovalHandler.setRequestFactory(DefaultOAuth2RequestFactory(clientDetailsService))
        return userApprovalHandler
    }
}