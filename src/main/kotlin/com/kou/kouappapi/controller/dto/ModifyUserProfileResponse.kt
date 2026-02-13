package com.kou.kouappapi.controller.dto

data class ModifyUserProfileResponse(
    val userId: Long,
    val profileImageUrl: String? = null,
)
