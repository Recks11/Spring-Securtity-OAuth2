package com.rexijie.oauth2.oauth2authserver.dao.token

import org.springframework.security.oauth2.common.util.SerializationUtils
import org.springframework.security.oauth2.provider.OAuth2Authentication
import java.util.*
/**
 * Utility class for Token manipulation
 * */
object TokenUtils {
    fun serializeAuthentication(authenticationObject: OAuth2Authentication?): String? {
        return try {
            val bytes: ByteArray = SerializationUtils.serialize(authenticationObject)
            Base64.getEncoder().encodeToString(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    fun deserializeAuthentication(encodedObject: String?): OAuth2Authentication? {
        return try {
            val bytes: ByteArray = Base64.getDecoder().decode(encodedObject)
            SerializationUtils.deserialize(bytes)
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}