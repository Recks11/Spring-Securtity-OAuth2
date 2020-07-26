package com.rexijie.oauth2.oauth2authserver.config

import com.rexijie.oauth2.oauth2authserver.repository.AccessTokenRepository
import com.rexijie.oauth2.oauth2authserver.repository.RefreshTokenRepository
import com.rexijie.oauth2.oauth2authserver.service.AuthClientDetailsService
import com.rexijie.oauth2.oauth2authserver.service.CustomUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore

/**
 * Authentication Server configuration class
 */
@Configuration
class Oauth2ServerConfig(val clientDetailsService: AuthClientDetailsService,
                         val userDetailsService: CustomUserDetailsService,
                         val accessTokenRepository: AccessTokenRepository,
                         val refreshTokenRepository: RefreshTokenRepository,
                         @Qualifier("authenticationManagerBean") val authenticationManager: AuthenticationManager
) : AuthorizationServerConfigurer {

    /**
     * ClientDetailsService used to provide a database of registered clients
     */
    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients!!.withClientDetails(clientDetailsService)
    }

    /**
     * Authorisation endpoint. This is used to handle requests for the password grant type
     * It uses the Autowired token store to manage tokens
     */
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints!!.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security!!.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
    }


    @Bean
    fun tokenStore(): TokenStore {
        return CustomTokenStore(
                accessTokenRepository = accessTokenRepository,
                refreshTokenRepository = refreshTokenRepository
        )
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices {
        val tokenServices = DefaultTokenServices()
        tokenServices.setTokenStore(tokenStore())
        tokenServices.setSupportRefreshToken(true)
        return tokenServices
    }
}