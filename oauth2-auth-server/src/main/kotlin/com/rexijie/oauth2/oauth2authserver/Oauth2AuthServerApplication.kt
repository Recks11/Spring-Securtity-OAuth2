package com.rexijie.oauth2.oauth2authserver

import com.rexijie.oauth2.oauth2authserver.dao.AuthClientDetails
import com.rexijie.oauth2.oauth2authserver.dao.UserAuthority
import com.rexijie.oauth2.oauth2authserver.dao.UserEntity
import com.rexijie.oauth2.oauth2authserver.repository.AuthClientDetailsRepository
import com.rexijie.oauth2.oauth2authserver.repository.CustomUserDetailsRepository
import com.rexijie.oauth2.oauth2authserver.utility.constants.AuthorityConstants
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashSet

@EnableAuthorizationServer
@SpringBootApplication
class Oauth2AuthServerApplication

fun main(args: Array<String>) {
    runApplication<Oauth2AuthServerApplication>(*args)
}

@Service
class BootStrap(val service: CustomUserDetailsRepository,
                val clientsRepo: AuthClientDetailsRepository,
                val encoder: PasswordEncoder)
    : CommandLineRunner {
    override fun run(vararg args: String?) {

        //Register a user
        val userRole = UserAuthority(0L, role = AuthorityConstants.USER_ROLE)
        val adminRole = UserAuthority(0L, role = AuthorityConstants.ADMIN_ROLE)
        val user = UserEntity(
                1L,
                UUID.randomUUID().toString(),
                "rex",
                encoder.encode("12345"),
                isEnabled = true,
                isAccountLocked = false,
                isAccountNonExpired = true,
                isCredentialsExpired = false,
                authorities = HashSet()
        )
        user.addAuthority(userRole)
        user.addAuthority(adminRole)
        service.deleteAll()
        service.save(user)

        service.findAll().forEach{println(it)}

        // Register a client
        val client = AuthClientDetails(
                id = UUID.randomUUID().toString(),
                clientId = "dev-resource-server",
                name = "development",
                accessTokenValiditySeconds = 1 * 60 * 60,
                refreshTokenValiditySeconds = 2 * 60 * 60,
                clientSecret = encoder.encode("dev-resource-secret"),
                additionalInformation = emptyMap(),
                authorisedGrantTypes = HashSet(listOf("refresh_token", "password")),
                authorities = arrayListOf("ROLE_USER", "ROLE_DEVELOPER"),
                scope = HashSet(arrayListOf("read","write","trust")),
                secretRequired = true,
                scoped = false
        )

        clientsRepo.deleteAll()
        clientsRepo.save(client)

    }
}