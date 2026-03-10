package com.kou.usagiappapi.repository

import com.kou.usagiappapi.entity.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshToken, Long> {
    fun findByUserId(userId: Long): RefreshToken?

    fun findByTokenHash(tokenHash: String): RefreshToken?

    fun deleteAllByUserId(userId: Long)
}
