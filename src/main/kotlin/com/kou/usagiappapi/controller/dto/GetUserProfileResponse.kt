package com.kou.usagiappapi.controller.dto

import com.kou.usagiappapi.service.dto.GetUserProfileResponseDto
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
