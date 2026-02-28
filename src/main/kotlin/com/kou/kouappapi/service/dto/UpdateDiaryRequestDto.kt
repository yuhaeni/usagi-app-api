package com.kou.kouappapi.service.dto

import com.kou.kouappapi.enums.Emotion
import org.springframework.web.multipart.MultipartFile

data class UpdateDiaryRequestDto(
    val emotion: Emotion? = null,
    val imageFile: MultipartFile? = null,
    val content: String? = null,
    val deleteImage: Boolean? = null,
)
