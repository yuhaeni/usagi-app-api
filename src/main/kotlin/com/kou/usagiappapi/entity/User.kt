package com.kou.usagiappapi.entity

import com.kou.usagiappapi.enums.Role
import com.kou.usagiappapi.enums.SocialProvider
import com.kou.usagiappapi.enums.UserStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(
    name = "users",
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
    var deletedAt: LocalDateTime? = null,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    fun update(
        name: String? = null,
        encodedPassword: String? = null,
        profileImageId: String? = null,
        status: UserStatus? = null,
        deletedAt: LocalDateTime? = null,
        deleteProfileImage: Boolean? = null,
    ) {
        if (deleteProfileImage == true) {
            this.profileImageId = null
        }
        name?.let { this.name = name }
        encodedPassword?.let { this.password = it }
        profileImageId?.let { this.profileImageId = it }
        status?.let { this.status = status }
        deletedAt?.let { this.deletedAt = deletedAt }
    }

    fun delete(profileImageId: String? = null) {
        profileImageId?.let { this.profileImageId = null }
    }
}
