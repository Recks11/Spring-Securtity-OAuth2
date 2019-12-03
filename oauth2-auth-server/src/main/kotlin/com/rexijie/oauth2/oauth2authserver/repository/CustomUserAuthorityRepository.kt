package com.rexijie.oauth2.oauth2authserver.repository

import com.rexijie.oauth2.oauth2authserver.dao.UserAuthority
import org.springframework.data.repository.CrudRepository

interface CustomUserAuthorityRepository: CrudRepository<UserAuthority, Long> {
    fun findByAuthorityId(id: String): UserAuthority
    fun findByRole(role: String): UserAuthority
}