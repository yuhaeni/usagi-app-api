package com.kou.kouappapi.auth.controller.dto

import com.kou.kouappapi.auth.service.dto.SocialLoginResponseDto

data class SocialLoginResponse(
    val accessToken: String,
    val refreshToken: String,
)

fun SocialLoginResponseDto.toResponse() =
    SocialLoginResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
