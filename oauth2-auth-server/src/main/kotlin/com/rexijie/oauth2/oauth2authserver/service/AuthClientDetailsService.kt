package com.rexijie.oauth2.oauth2authserver.service

import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService

interface AuthClientDetailsService: ClientDetailsService {
    override fun loadClientByClientId(clientId: String?): ClientDetails
}