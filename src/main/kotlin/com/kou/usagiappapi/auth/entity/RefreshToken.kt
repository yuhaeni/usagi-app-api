package com.kou.usagiappapi.auth.entity

import com.kou.usagiappapi.shared.entity.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.Date

@Entity
class RefreshToken(
    val userId: Long,
    @Column(length = 500)
    val tokenHash: String,
    val expiresAt: Date,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
}
