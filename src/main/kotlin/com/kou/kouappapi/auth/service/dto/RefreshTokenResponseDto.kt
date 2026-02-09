package com.kou.kouappapi.auth.service.dto

data class RefreshTokenResponseDto(
    val accessToken: String,
    val refreshToken: String,
)
