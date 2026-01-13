package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.auth.service.dto.CompleteProfileRequestDto
import org.springframework.web.multipart.MultipartFile

data class CompleteProfileRequest(
    val userId: Long,
    val name: String,
    val password: String?,
    val profileFile: MultipartFile?,
)

fun CompleteProfileRequest.toDto(): CompleteProfileRequestDto =
    CompleteProfileRequestDto(
        userId = userId,
        name = name,
        password = password,
        profileFile = profileFile
    )
