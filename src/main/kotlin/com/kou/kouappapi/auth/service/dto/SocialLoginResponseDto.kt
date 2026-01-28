package com.kou.kouappapi.auth.service.dto

import com.kou.kouappapi.enums.LoginNextStep

data class SocialLoginResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val nextStep: LoginNextStep,
)
