package com.kou.kouappapi.auth.service.dto

data class SocialLoginResponseDto(
    val accessToken: String,
    val refreshToken: String,
)
