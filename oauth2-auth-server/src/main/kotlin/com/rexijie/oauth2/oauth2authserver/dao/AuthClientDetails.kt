package com.rexijie.oauth2.oauth2authserver.dao

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.persistence.Id
import kotlin.collections.HashSet

@Document
data class AuthClientDetails(
        @Id
        var id: String,
        var name: String,
        var clientId: String,
        var clientSecret: String,
        var secretRequired: Boolean,
        var scoped: Boolean,
        var scope: Set<String> = HashSet(),
        var resourceIds: Set<String> =  HashSet(),
        var authorisedGrantTypes: Set<String> = HashSet(),
        var registeredRedirectUris:  Set<String> = HashSet(),
        var autoApproveScopes: Set<String> = HashSet(),
        var authorities: Collection<String> = HashSet(),
        var accessTokenValiditySeconds: Int,
        var refreshTokenValiditySeconds: Int,
        var additionalInformation: Map<String, Any> = HashMap()
)