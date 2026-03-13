package com.kou.usagiappapi.activityCategory.service.dto

import com.kou.usagiappapi.activityCategory.controller.dto.GetActivityCategoriesResponse

data class GetActivityCategoriesResponseDto(
    val activityCategoryId: Long,
    val name: String,
    val userId: Long? = 0L,
)

fun List<GetActivityCategoriesResponseDto>.toResponse(): List<GetActivityCategoriesResponse> =
    map {
        GetActivityCategoriesResponse(
            activityCategoryId = it.activityCategoryId,
            name = it.name,
            userId = it.userId,
        )
    }
