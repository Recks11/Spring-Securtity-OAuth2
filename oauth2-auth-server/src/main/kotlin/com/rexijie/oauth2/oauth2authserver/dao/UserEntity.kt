package com.rexijie.oauth2.oauth2authserver.dao

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant
import java.util.*
import javax.persistence.*
import kotlin.collections.HashSet

/**
 * Class which represents Users in the database
 */

@Table(name = "Users")
@Entity(name = "users")
data class UserEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,
        val userId: String,
        private var username: String,
        private var password: String,
        @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id")
        private var authorities: Set<UserAuthority> = HashSet(),
        private var isEnabled: Boolean,
        private var isCredentialsExpired: Boolean,
        private var isAccountNonExpired: Boolean,
        private var isAccountLocked: Boolean,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        val created: Date = Date.from(Instant.now())
) : UserDetails {

    override fun getUsername(): String {
        return username
    }

    override fun getPassword(): String {
        return password
    }

    override fun getAuthorities(): Set<UserAuthority> {
        return authorities
    }

    fun addAuthority(authority: UserAuthority) {
        authority.user = this
        authorities = authorities.plus(authority)
    }
    override fun isEnabled(): Boolean {
        return isEnabled
    }

    override fun isCredentialsNonExpired(): Boolean {
        return !isCredentialsExpired
    }

    override fun isAccountNonExpired(): Boolean {
        return isAccountNonExpired
    }

    override fun isAccountNonLocked(): Boolean {
        return !isAccountLocked
    }
}