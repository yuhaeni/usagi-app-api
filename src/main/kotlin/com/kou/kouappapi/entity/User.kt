package com.kou.kouappapi.entity

import com.kou.kouappapi.auth.service.dto.UserResponseDto
import com.kou.kouappapi.enums.Role
import com.kou.kouappapi.enums.SocialProvider
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var name: String? = null,
    @Column(nullable = false, unique = true, updatable = false)
    val email: String,
    @Enumerated(EnumType.STRING)
    val provider: SocialProvider,
    val providerId: String,
    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER,
    @Column(nullable = false)
    var isRegistrationCompleted: Boolean = false,
    @Column(nullable = false)
    var isDeleted: Boolean = false,
) : BaseEntity()

fun User.toResponseDto(): UserResponseDto =
    UserResponseDto(
        id = id,
        email = email,
        name = name,
    )

// TODO 프로필 업데이트 관련 함수 추가
