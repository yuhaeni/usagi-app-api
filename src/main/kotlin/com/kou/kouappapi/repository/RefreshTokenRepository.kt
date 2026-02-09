package com.kou.kouappapi.repository

import com.kou.kouappapi.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByUserId(userId: Long): RefreshToken?

    fun findByTokenHash(tokenHash: String): RefreshToken?
}
