package com.kou.usagiappapi.auth.controller.dto

import com.kou.usagiappapi.auth.service.dto.RefreshTokenRequestDto

data class RefreshTokenRequest(
    val refreshToken: String,
)

fun RefreshTokenRequest.toDto(): RefreshTokenRequestDto =
    RefreshTokenRequestDto(
        refreshToken = refreshToken,
    )
