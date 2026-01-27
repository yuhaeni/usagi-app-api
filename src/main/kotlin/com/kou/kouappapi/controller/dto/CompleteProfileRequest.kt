package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.auth.service.dto.CompleteProfileRequestDto
import org.springframework.web.multipart.MultipartFile

data class CompleteProfileRequest(
    val name: String,
    val password: String?,
    val profileFile: MultipartFile?,
)

fun CompleteProfileRequest.toDto(): CompleteProfileRequestDto =
    CompleteProfileRequestDto(
        name = name,
        password = password,
        profileFile = profileFile,
    )
