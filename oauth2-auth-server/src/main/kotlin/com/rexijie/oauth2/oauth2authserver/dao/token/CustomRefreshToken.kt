package com.rexijie.oauth2.oauth2authserver.dao.token

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.oauth2.common.OAuth2RefreshToken
import org.springframework.security.oauth2.provider.OAuth2Authentication

@Document(collection= "refreshtokens")
data class CustomRefreshToken(
        @Id
        var id: String,
        var tokenId: String,
        var refreshToken: OAuth2RefreshToken
) {
    private lateinit var authentication: String

    fun getAuthentication(): OAuth2Authentication? {
        return TokenUtils.deserializeAuthentication(authentication)
    }

    fun setAuthentication(authentication: OAuth2Authentication?) {
        this.authentication = TokenUtils.serializeAuthentication(authentication)!!
    }
}