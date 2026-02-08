package com.kou.kouappapi.auth.controller.dto

import com.kou.kouappapi.auth.service.dto.RefreshTokenRequestDto

data class RefreshTokenRequest(
    val refreshToken: String,
)

fun RefreshTokenRequest.toDto(): RefreshTokenRequestDto =
    RefreshTokenRequestDto(
        refreshToken = refreshToken,
    )
