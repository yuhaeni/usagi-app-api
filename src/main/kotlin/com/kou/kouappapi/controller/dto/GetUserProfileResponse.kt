package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.service.dto.GetUserProfileResponseDto
import kotlin.String

data class GetUserProfileResponse(
    val userId: Long,
    val name: String? = null,
    val email: String,
    val profileImageUrl: String? = null,
)

fun GetUserProfileResponseDto.toResponse(): GetUserProfileResponse =
    GetUserProfileResponse(
        userId = userId,
        name = name,
        email = email,
        profileImageUrl = profileImageUrl,
    )
