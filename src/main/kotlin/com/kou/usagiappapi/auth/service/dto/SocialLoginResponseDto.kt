package com.kou.usagiappapi.auth.service.dto

data class SocialLoginResponseDto(
    val accessToken: String,
    val refreshToken: String,
)
