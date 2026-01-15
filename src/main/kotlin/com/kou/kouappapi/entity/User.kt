package com.kou.kouappapi.entity

import com.kou.kouappapi.auth.service.dto.UserResponseDto
import com.kou.kouappapi.enums.Role
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.enums.UserStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint

@Entity
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["provider", "provider_id"]),
        UniqueConstraint(columnNames = ["email"]),
    ],
)
class User(
    var name: String? = null,
    var profileImageId: String? = null,
    @Column(nullable = false, unique = true, updatable = false)
    val email: String,
    var password: String? = null,
    @Enumerated(EnumType.STRING)
    val provider: SocialProvider,
    val providerId: String,
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER,
    @Enumerated(EnumType.STRING)
    var status: UserStatus = UserStatus.ACTIVE,
    @Column(nullable = false)
    var profileCompleted: Boolean = false,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    fun completeProfile(
        name: String,
        encodedPassword: String?,
        profileImageId: String?,
    ) {
        this.name = name
        this.profileCompleted = true
        encodedPassword?.let { this.password = it }
        profileImageId?.let { this.profileImageId = it }
    }
}

fun User.toResponseDto(): UserResponseDto =
    UserResponseDto(
        id = id,
        email = email,
        name = name,
    )
