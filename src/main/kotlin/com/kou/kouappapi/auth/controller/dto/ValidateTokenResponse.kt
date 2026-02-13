package com.kou.kouappapi.auth.controller.dto

import com.kou.kouappapi.auth.service.dto.ValidateTokenResponseDto

data class ValidateTokenResponse(
    val isValidToken: Boolean,
)

fun ValidateTokenResponseDto.toResponse(): ValidateTokenResponse = ValidateTokenResponse(isValidToken = isValidToken)
