package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.service.dto.CompleteProfileResponseDto

data class CompleteProfileResponse(
    val userId: Long,
    val email: String?,
    val userName: String?,
    val profileImageUrl: String?,
)

fun CompleteProfileResponseDto.toResponse() =
    CompleteProfileResponse(
        userId = userId,
        email = email,
        userName = userName,
        profileImageUrl = profileImageUrl,
    )
