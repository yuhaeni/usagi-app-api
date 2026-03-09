package com.kou.kouappapi.service.dto

import com.kou.kouappapi.controller.dto.GetActivityCategoryListResponse

data class GetActivityCategoryListResponseDto(
    val activityCategoryId: Long,
    val name: String,
    val userId: Long? = 0L,
)

fun List<GetActivityCategoryListResponseDto>.toResponse(): List<GetActivityCategoryListResponse> =
    map {
        GetActivityCategoryListResponse(
            activityCategoryId = it.activityCategoryId,
            name = it.name,
            userId = it.userId,
        )
    }
