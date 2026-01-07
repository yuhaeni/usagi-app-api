package com.kou.kouappapi.auth.controller.dto

import com.kou.kouappapi.auth.service.dto.SocialLoginResponseDto
import com.kou.kouappapi.auth.service.dto.UserResponseDto
import com.kou.kouappapi.enums.LoginNextStep

data class SocialLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val nextStep: LoginNextStep,
    val user: UserResponseDto,
)

fun SocialLoginResponseDto.toResponse() =
    SocialLoginResponse(
        accessToken = accessToken,
        refreshToken = refreshToken,
        nextStep = nextStep,
        user = user,
    )
