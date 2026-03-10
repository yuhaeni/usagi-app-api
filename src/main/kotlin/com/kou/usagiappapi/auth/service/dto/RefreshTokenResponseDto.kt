package com.kou.usagiappapi.auth.service.dto

data class RefreshTokenResponseDto(
    val accessToken: String,
    val refreshToken: String,
)
