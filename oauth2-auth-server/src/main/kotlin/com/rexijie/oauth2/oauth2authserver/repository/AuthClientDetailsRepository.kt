package com.rexijie.oauth2.oauth2authserver.repository

import com.rexijie.oauth2.oauth2authserver.dao.AuthClientDetails
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface AuthClientDetailsRepository: MongoRepository<AuthClientDetails, String> {
    fun findByClientId(clientId: String): AuthClientDetails
}