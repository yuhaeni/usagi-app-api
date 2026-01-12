package com.kou.kouappapi.auth.service.dto

import org.springframework.web.multipart.MultipartFile

data class CompleteProfileRequestDto(
    val userId: Long,
    val name: String,
    val password: String? = null,
    val profileFile: MultipartFile? = null,
    val profileComplete: Boolean = true,
)
