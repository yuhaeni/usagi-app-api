package com.kou.kouappapi.service.dto

import org.springframework.web.multipart.MultipartFile

data class ModifyUserProfileRequestDto(
    val name: String? = null,
    val password: String? = null,
    val profileImageFile: MultipartFile? = null,
)
