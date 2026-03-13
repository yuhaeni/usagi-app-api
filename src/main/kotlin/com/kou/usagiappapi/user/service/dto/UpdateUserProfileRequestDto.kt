package com.kou.usagiappapi.user.service.dto

import org.springframework.web.multipart.MultipartFile

data class UpdateUserProfileRequestDto(
    val name: String? = null,
    val password: String? = null,
    val profileImageFile: MultipartFile? = null,
    val deleteProfileImage: Boolean? = null,
)
