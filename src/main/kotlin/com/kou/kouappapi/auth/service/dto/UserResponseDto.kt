package com.kou.kouappapi.auth.service.dto

data class UserResponseDto(
    val id: Long,
    val email: String,
    val name: String?,
    val coupleId: Long?,
)
