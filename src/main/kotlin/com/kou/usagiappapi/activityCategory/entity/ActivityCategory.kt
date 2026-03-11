package com.kou.usagiappapi.activityCategory.entity

import com.kou.usagiappapi.activityCategory.service.dto.ActivityCategoryResponseDto
import com.kou.usagiappapi.common.entity.BaseEntity
import com.kou.usagiappapi.user.entity.User
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class ActivityCategory(
    val name: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
}

fun List<ActivityCategory>.toResponseDto(): List<ActivityCategoryResponseDto> =
    map {
        ActivityCategoryResponseDto(
            id = it.id,
            name = it.name,
        )
    }
