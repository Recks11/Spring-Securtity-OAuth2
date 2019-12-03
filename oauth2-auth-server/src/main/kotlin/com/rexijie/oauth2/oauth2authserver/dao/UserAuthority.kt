package com.rexijie.oauth2.oauth2authserver.dao

import org.springframework.security.core.GrantedAuthority
import java.util.*
import javax.persistence.*

/**
 * Class to represent user authorities stored in the database.
 *
 * @property role signifies the role granted to the {@link UserDetails}
 *
 * User authorities are stored in the database and each user will hold a reference to their individual authority
 * which will be pulled when needed
 */

@Entity(name = "authorities")
@Table(name = "Authorities")
data class UserAuthority(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long,
        @Column(name = "authority_uuid")
        var authorityId: String = UUID.randomUUID().toString(),
        private var role: String
) : GrantedAuthority {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    lateinit var user: UserEntity

    override fun getAuthority(): String = role
}