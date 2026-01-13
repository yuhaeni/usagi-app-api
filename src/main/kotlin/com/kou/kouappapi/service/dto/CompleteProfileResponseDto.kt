package com.kou.kouappapi.service.dto

data class CompleteProfileResponseDto(
    val userId: Long,
    val email: String?,
    val userName: String?,
    val profileImageUrl: String?,
)
