package com.kou.kouappapi.auth.controller.dto

import com.kou.kouappapi.auth.service.dto.ValidateTokenResponseDto

data class ValidateTokenResponse(
    val validToken: Boolean,
)

fun ValidateTokenResponseDto.toResponse(): ValidateTokenResponse = ValidateTokenResponse(validToken = validToken)
