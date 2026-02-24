package com.kou.kouappapi.service.dto

import com.kou.kouappapi.enums.Emotion
import org.springframework.web.multipart.MultipartFile

data class UpdateDiaryRequestDto(
    val emotion: Emotion,
    val imageFile: MultipartFile? = null,
    val content: String? = null,
)
