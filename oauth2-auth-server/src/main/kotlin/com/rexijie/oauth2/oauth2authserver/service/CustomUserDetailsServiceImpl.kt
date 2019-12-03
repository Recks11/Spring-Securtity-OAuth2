package com.rexijie.oauth2.oauth2authserver.service

import com.rexijie.oauth2.oauth2authserver.dao.UserEntity
import com.rexijie.oauth2.oauth2authserver.repository.CustomUserAuthorityRepository
import com.rexijie.oauth2.oauth2authserver.repository.CustomUserDetailsRepository
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsServiceImpl(val repository: CustomUserDetailsRepository) : CustomUserDetailsService {

    override fun loadUserByUsername(username: String?): UserEntity {
        return repository.findByUsername(username!!)
    }

    override fun saveUser(user: UserEntity): UserEntity = repository.save(user)
}