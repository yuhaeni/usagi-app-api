package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.service.dto.UpdateUserProfileRequestDto
import org.springframework.web.multipart.MultipartFile

data class UpdateUserProfileRequest(
    val name: String? = null,
    val password: String? = null,
    val profileImageFile: MultipartFile? = null,
)

fun UpdateUserProfileRequest.toDto(): UpdateUserProfileRequestDto =
    UpdateUserProfileRequestDto(
        name = name,
        password = password,
        profileImageFile = profileImageFile,
    )
