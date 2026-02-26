package com.kou.kouappapi.repository

import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.enums.UserStatus
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
