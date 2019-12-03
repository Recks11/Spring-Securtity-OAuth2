package com.rexijie.oauth2.oauth2authserver.dao.token

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication

@Document(collection= "accesstokens")
data class CustomAccessToken(
        @Id
        var id: String,
        var tokenId: String,
        var token: OAuth2AccessToken,
        var authenticationId: String,
        var username: String?,
        var clientId: String,
        var refreshToken: String
) {
    private lateinit var authentication: String

    fun getAuthentication(): OAuth2Authentication? {
        return TokenUtils.deserializeAuthentication(authentication)
    }

    fun setAuthentication(authentication: OAuth2Authentication?) {
        this.authentication = TokenUtils.serializeAuthentication(authentication)!!
    }
}