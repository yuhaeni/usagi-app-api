package com.kou.kouappapi.auth.controller.dto

import com.kou.kouappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.kouappapi.enums.SocialProvider
import io.swagger.v3.oas.annotations.media.Schema

data class SocialLoginRequest(
    @Schema(
        description = "제공사",
        allowableValues = ["GOOGLE", "APPLE"],
        example = "GOOGLE",
        required = true,
    )
    val provider: SocialProvider,
    @Schema(description = "제공사의 로그인 토큰 (구글은 id_token)", required = true)
    val token: String,
)

fun SocialLoginRequest.toDto() =
    SocialLoginRequestDto(
        provider = provider,
        token = token,
    )
