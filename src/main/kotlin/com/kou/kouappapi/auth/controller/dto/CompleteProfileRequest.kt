package com.kou.kouappapi.auth.controller.dto

import com.kou.kouappapi.auth.service.dto.CompleteProfileRequestDto

data class CompleteProfileRequest(
    val userId: Long,
    val name: String,
    val password: String?,
    val profileImageUrl: String?,
)

fun CompleteProfileRequest.toDto(): CompleteProfileRequestDto =
    CompleteProfileRequestDto(
        userId = userId,
        name = name,
        password = password,
        profileImageUrl = profileImageUrl,
    )
