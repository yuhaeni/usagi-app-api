package com.kou.kouappapi.auth.controller.dto

import com.kou.kouappapi.auth.service.dto.SocialLoginResponseDto
import com.kou.kouappapi.enums.LoginNextStep

data class SocialLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val nextStep: LoginNextStep,
)

fun SocialLoginResponseDto.toResponse() =
    SocialLoginResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
        nextStep = nextStep,
    )
