package com.kou.usagiappapi.auth.controller.dto

import com.kou.usagiappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.usagiappapi.enums.SocialProvider
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
