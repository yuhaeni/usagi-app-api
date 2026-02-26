package com.kou.kouappapi.controller.dto

data class UpdateUserProfileResponse(
    val userId: Long,
    val profileImageUrl: String? = null,
)
