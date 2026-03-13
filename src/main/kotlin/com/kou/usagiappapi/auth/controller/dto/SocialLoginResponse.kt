package com.kou.usagiappapi.auth.controller.dto

import com.kou.usagiappapi.auth.service.dto.SocialLoginResponseDto

data class SocialLoginResponse(
    val accessToken: String,
    val refreshToken: String,
)

fun SocialLoginResponseDto.toResponse() =
    SocialLoginResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
