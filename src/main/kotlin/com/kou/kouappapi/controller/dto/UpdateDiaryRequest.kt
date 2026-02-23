package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.enums.Emotion
import com.kou.kouappapi.service.dto.UpdateDiaryRequestDto
import org.springframework.web.multipart.MultipartFile

data class UpdateDiaryRequest(
    val emotion: Emotion,
    val imageFile: MultipartFile? = null,
    val content: String? = null,
)

fun UpdateDiaryRequest.toDto() =
    UpdateDiaryRequestDto(
        emotion = emotion,
        imageFile = imageFile,
        content = content,
    )
