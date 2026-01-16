package com.kou.kouappapi.auth.service.dto

import com.kou.kouappapi.enums.SocialProvider

data class SocialLoginRequestDto(
    val provider: SocialProvider,
    val token: String,
    val inviteCode: String? = null,
)
