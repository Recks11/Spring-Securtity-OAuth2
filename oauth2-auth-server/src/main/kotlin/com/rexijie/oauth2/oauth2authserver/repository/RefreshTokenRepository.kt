package com.rexijie.oauth2.oauth2authserver.repository

import com.rexijie.oauth2.oauth2authserver.dao.token.CustomRefreshToken
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface RefreshTokenRepository: MongoRepository<CustomRefreshToken, String> {
    fun findByTokenId(tokenId: String): Optional<CustomRefreshToken>
}