package com.kou.usagiappapi.user.repository

import com.kou.usagiappapi.enums.SocialProvider
import com.kou.usagiappapi.enums.UserStatus
import com.kou.usagiappapi.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByProviderAndProviderIdAndStatus(
        provider: SocialProvider,
        providerId: String,
        status: UserStatus,
    ): User?

    fun findByEmail(email: String): User?

    fun findByProviderAndEmail(
        provider: SocialProvider,
        email: String,
    ): User?
}
