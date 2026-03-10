package com.kou.usagiappapi.user.controller.dto

data class UpdateUserProfileResponse(
    val userId: Long,
    val profileImageUrl: String? = null,
)
