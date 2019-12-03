package com.rexijie.oauth2.oauth2authserver.service

import com.rexijie.oauth2.oauth2authserver.dao.UserEntity
import org.springframework.security.core.userdetails.UserDetailsService

interface CustomUserDetailsService: UserDetailsService {

    override fun loadUserByUsername(username: String?): UserEntity

    fun saveUser(user: UserEntity): UserEntity
}