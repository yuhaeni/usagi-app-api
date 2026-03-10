package com.kou.usagiappapi.controller.dto

data class UpdateUserProfileResponse(
    val userId: Long,
    val profileImageUrl: String? = null,
)
