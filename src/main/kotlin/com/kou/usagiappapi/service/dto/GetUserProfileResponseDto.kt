package com.kou.usagiappapi.service.dto

data class GetUserProfileResponseDto(
    val userId: Long,
    val name: String? = null,
    val email: String,
    var profileImageUrl: String? = null,
)
