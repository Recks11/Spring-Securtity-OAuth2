package com.rexijie.oauth2.oauth2authserver.config

import com.rexijie.oauth2.oauth2authserver.dao.token.CustomAccessToken
import com.rexijie.oauth2.oauth2authserver.dao.token.CustomRefreshToken
import com.rexijie.oauth2.oauth2authserver.repository.AccessTokenRepository
import com.rexijie.oauth2.oauth2authserver.repository.RefreshTokenRepository
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2RefreshToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator
import org.springframework.security.oauth2.provider.token.TokenStore
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.collections.ArrayList


class CustomTokenStore(
        private val accessTokenRepository: AccessTokenRepository,
        private val refreshTokenRepository: RefreshTokenRepository
) : TokenStore {

    private val authenticationKeyGenerator = DefaultAuthenticationKeyGenerator()

    override fun getAccessToken(authentication: OAuth2Authentication?): OAuth2AccessToken? {
        var accessToken: OAuth2AccessToken? = null
        val authenticationId = authenticationKeyGenerator.extractKey(authentication)
        val token = accessTokenRepository.findByAuthenticationId(authenticationId)
        if (token!!.isPresent) {
            accessToken = token.get().token
            if (authenticationId == authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken))) {
                removeAccessToken(accessToken)
            }
        }
        return accessToken
    }

    override fun findTokensByClientIdAndUserName(clientId: String?, userName: String?): MutableCollection<OAuth2AccessToken>? {
        val tokenCollection = ArrayList<OAuth2AccessToken>()
        val tokens = accessTokenRepository.findByClientIdAndUsername(clientId, userName)!!
        tokens.forEach { tokenCollection.add(it!!.token) }
        return tokenCollection
    }

    override fun findTokensByClientId(clientId: String?): MutableCollection<OAuth2AccessToken>? {
        val tokenCollection = ArrayList<OAuth2AccessToken>()
        val tokens = accessTokenRepository.findByClientId(clientId)!!
        tokens.forEach { tokenCollection.add(it!!.token) }
        return tokenCollection
    }

    override fun readAccessToken(tokenValue: String?): OAuth2AccessToken? {
        val accessToken = accessTokenRepository.findByTokenId(extractTokenKey(tokenValue))!!
        return if (accessToken.isPresent) accessToken.get().token else null
    }

    override fun readRefreshToken(tokenValue: String?): OAuth2RefreshToken? {
        val tokenId = extractTokenKey(tokenValue)
        val refreshToken = refreshTokenRepository.findByTokenId(tokenId!!)

        return if (refreshToken.isPresent) refreshToken.get().refreshToken else null;
    }

    override fun readAuthentication(token: OAuth2AccessToken?): OAuth2Authentication? {
        return readAuthentication(token!!.value)
    }

    override fun readAuthentication(token: String): OAuth2Authentication? {
        val tokenId = extractTokenKey(token)
        val accessToken = accessTokenRepository.findByTokenId(tokenId)!!
        if (accessToken.isPresent) {
            return accessToken.get().getAuthentication()
        }
        return null
    }

    override fun readAuthenticationForRefreshToken(token: OAuth2RefreshToken?): OAuth2Authentication? {
        val tokenId = extractTokenKey(token!!.value)!!
        val refreshToken = refreshTokenRepository.findByTokenId(tokenId)
        return if (refreshToken.isPresent)
            refreshToken.get().getAuthentication()!!
        else
            null
    }

    override fun storeRefreshToken(refreshToken: OAuth2RefreshToken?, authentication: OAuth2Authentication?) {
        val refreshTokenKey = extractTokenKey(refreshToken!!.value)!!
        if (readRefreshToken(refreshToken.value) != null) this.removeRefreshToken(refreshToken)

        val customRefreshToken = CustomRefreshToken(
                id = UUID.randomUUID().toString(),
                tokenId = refreshTokenKey,
                refreshToken = refreshToken
        )
        customRefreshToken.setAuthentication(authentication)
        refreshTokenRepository.save(customRefreshToken)
    }

    override fun storeAccessToken(token: OAuth2AccessToken?, authentication: OAuth2Authentication?) {
        val refreshToken = token!!.refreshToken.value
        val accessTokenId = extractTokenKey(token.value)!!
        if (readAccessToken(token.value) != null) this.removeAccessToken(token)

        val customToken = CustomAccessToken(
                id = UUID.randomUUID().toString(),
                tokenId = accessTokenId,
                token = token,
                authenticationId = authenticationKeyGenerator.extractKey(authentication),
                username = if (authentication!!.isClientOnly) null else authentication.name,
                clientId = authentication.oAuth2Request.clientId,
                refreshToken = extractTokenKey(refreshToken)!!
        )
        customToken.setAuthentication(authentication)
        accessTokenRepository.save(customToken)
    }

    override fun removeRefreshToken(token: OAuth2RefreshToken?) {
        val tokenKey = extractTokenKey(token!!.value)!!
        val refreshToken = refreshTokenRepository.findByTokenId(tokenKey)
        if (refreshToken.isPresent) refreshTokenRepository.delete(refreshToken.get())
    }

    override fun removeAccessTokenUsingRefreshToken(refreshToken: OAuth2RefreshToken?) {
        val tokenKey = extractTokenKey(refreshToken!!.value)!!
        val accessToken = accessTokenRepository.findByRefreshToken(tokenKey)!!
        if (accessToken.isPresent) accessTokenRepository.delete(accessToken.get())
    }

    override fun removeAccessToken(token: OAuth2AccessToken?) {
        val tokenId = extractTokenKey(token!!.value)!!
        val accessToken = accessTokenRepository.findByTokenId(tokenId)!!
        if (accessToken.isPresent) accessTokenRepository.delete(accessToken.get())
    }


    companion object {
        fun extractTokenKey(value: String?): String? {
            if (value == null) {
                return null
            } else {
                val digest: MessageDigest
                try {
                    digest = MessageDigest.getInstance("MD5")
                } catch (ex: NoSuchAlgorithmException) {
                    throw IllegalStateException("MD5 Algorithm not available")
                }

                try {
                    val b: ByteArray = digest.digest(value.toByteArray(Charsets.UTF_8))
                    return String.format("%032x", BigInteger(1, b))
                } catch (ex: UnsupportedEncodingException) {
                    throw IllegalStateException("Cannot encode token to UTF-8")
                }
            }
        }
    }
}