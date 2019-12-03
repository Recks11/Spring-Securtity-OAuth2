package com.rexijie.oauth2.oauth2authserver.repository

import com.rexijie.oauth2.oauth2authserver.dao.token.CustomAccessToken
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface AccessTokenRepository : MongoRepository<CustomAccessToken, String> {
    fun findByClientId(clientId: String?): List<CustomAccessToken?>?

    fun findByClientIdAndUsername(clientId: String?, username: String?): List<CustomAccessToken?>?

    fun findByTokenId(tokenId: String?): Optional<CustomAccessToken?>?

    fun findByRefreshToken(refreshToken: String?): Optional<CustomAccessToken?>?

    fun findByAuthenticationId(authenticationId: String?): Optional<CustomAccessToken?>?
}