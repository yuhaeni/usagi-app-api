package com.kou.usagiappapi.user.controller.dto

import com.kou.usagiappapi.user.service.dto.UpdateUserProfileRequestDto
import org.springframework.web.multipart.MultipartFile

data class UpdateUserProfileRequest(
    val name: String? = null,
    val password: String? = null,
    val profileImageFile: MultipartFile? = null,
    val deleteProfileImage: Boolean? = null,
)

fun UpdateUserProfileRequest.toDto(): UpdateUserProfileRequestDto =
    UpdateUserProfileRequestDto(
        name = name,
        password = password,
        profileImageFile = profileImageFile,
        deleteProfileImage = deleteProfileImage,
    )
