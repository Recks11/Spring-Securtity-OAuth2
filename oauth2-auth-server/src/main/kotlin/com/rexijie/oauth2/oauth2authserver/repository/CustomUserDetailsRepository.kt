package com.rexijie.oauth2.oauth2authserver.repository

import com.rexijie.oauth2.oauth2authserver.dao.UserEntity
import org.springframework.data.repository.CrudRepository

interface CustomUserDetailsRepository: CrudRepository<UserEntity, Long> {
    /**
     * @param username the username of the item as stored in the database
     */
    fun findByUsername(username: String): UserEntity
}