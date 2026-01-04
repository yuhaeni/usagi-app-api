package com.kou.kouappapi.repository

import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByProviderAndProviderId(
        provider: SocialProvider,
        providerId: String,
    ): User?

    fun findByEmail(email: String): User?
    fun findByProviderAndEmail(provider: SocialProvider, email: String): User?
}
