package com.kou.usagiappapi.activityCategory.service.dto

import com.kou.usagiappapi.activityCategory.controller.dto.ActivityCategoryResponse

data class ActivityCategoryResponseDto(
    val id: Long,
    val name: String,
)

fun List<ActivityCategoryResponseDto>.toResponse(): List<ActivityCategoryResponse> =
    map {
        ActivityCategoryResponse(
            id = it.id,
            name = it.name,
        )
    }
