package com.rexijie.oauth2.oauth2authserver.service

import com.rexijie.oauth2.oauth2authserver.repository.AuthClientDetailsRepository
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.client.BaseClientDetails
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class AuthClientDetailsServiceImpl(
        val clientDetailsRepository: AuthClientDetailsRepository
) : AuthClientDetailsService {

    override fun loadClientByClientId(clientId: String?): ClientDetails {
        val clientIdentifier = clientId!!

        val savedClient = clientDetailsRepository.findByClientId(clientIdentifier)

        val clientDetails = BaseClientDetails(
                savedClient.clientId,
                stringCollectionToString(savedClient.resourceIds),
                stringCollectionToString(savedClient.scope),
                stringCollectionToString(savedClient.authorisedGrantTypes),
                stringCollectionToString(savedClient.authorities))
        clientDetails.clientSecret = savedClient.clientSecret
        clientDetails.accessTokenValiditySeconds = savedClient.accessTokenValiditySeconds
        clientDetails.refreshTokenValiditySeconds = savedClient.refreshTokenValiditySeconds
        clientDetails.setAutoApproveScopes(savedClient.autoApproveScopes)
        clientDetails.additionalInformation = savedClient.additionalInformation
        clientDetails.registeredRedirectUri = savedClient.registeredRedirectUris


        return clientDetails
    }

    companion object {
        fun stringCollectionToString(col: Collection<String>): String {
            return col.stream().collect(Collectors.joining(","))
        }
    }
}