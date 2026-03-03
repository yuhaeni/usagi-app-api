package com.kou.kouappapi.service.dto

import com.kou.kouappapi.controller.dto.GetActivityCategoryListResponse

data class GetActivityCategoryListResponseDto(
    val name: String,
    val userId: Long? = 0L,
)

fun List<GetActivityCategoryListResponseDto>.toResponse(): List<GetActivityCategoryListResponse> =
    map {
        GetActivityCategoryListResponse(
            name = it.name,
            userId = it.userId,
        )
    }
