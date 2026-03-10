package com.kou.usagiappapi.auth.controller.dto

import com.kou.usagiappapi.auth.service.dto.RefreshTokenResponseDto

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)

fun RefreshTokenResponseDto.toResponse() =
    RefreshTokenResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
    )
