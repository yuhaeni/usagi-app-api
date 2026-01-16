package com.kou.kouappapi.auth.controller.dto

import com.kou.kouappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.kouappapi.enums.SocialProvider

data class SocialLoginRequest(
    val provider: SocialProvider,
    val token: String,
    val inviteCode: String?,
)

fun SocialLoginRequest.toDto() =
    SocialLoginRequestDto(
        provider = provider,
        token = token,
        inviteCode = inviteCode,
    )
