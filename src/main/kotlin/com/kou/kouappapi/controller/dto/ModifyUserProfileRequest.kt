package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.service.dto.ModifyUserProfileRequestDto
import org.springframework.web.multipart.MultipartFile

data class ModifyUserProfileRequest(
    val name: String? = null,
    val password: String? = null,
    val profileImageFile: MultipartFile? = null,
)

fun ModifyUserProfileRequest.toDto(): ModifyUserProfileRequestDto =
    ModifyUserProfileRequestDto(
        name = name,
        password = password,
        profileImageFile = profileImageFile,
    )
