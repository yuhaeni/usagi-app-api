package com.kou.kouappapi.auth.service.dto

data class CompleteProfileRequestDto(
    val userId: Long,
    val name: String,
    val password: String?,
    val profileImageUrl: String?,
    val profileComplete: Boolean = true,
)
