package com.kou.kouappapi.service.dto

import com.kou.kouappapi.controller.dto.ActivityCategoryResponse

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
