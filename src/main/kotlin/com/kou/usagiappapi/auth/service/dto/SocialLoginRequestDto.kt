package com.kou.usagiappapi.auth.service.dto

import com.kou.usagiappapi.user.enums.SocialProvider

data class SocialLoginRequestDto(
    val provider: SocialProvider,
    val token: String,
)
