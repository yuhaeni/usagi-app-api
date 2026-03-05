package com.kou.kouappapi.service.dto

import com.kou.kouappapi.enums.Emotion
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

data class CreateDiaryRequestDto(
    val date: LocalDate,
    val emotion: Emotion,
    val imageFile: MultipartFile? = null,
    val content: String? = null,
    val activityCategoryIdList: List<Long> = emptyList(),
)
