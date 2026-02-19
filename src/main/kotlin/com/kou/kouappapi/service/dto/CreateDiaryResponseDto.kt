package com.kou.kouappapi.service.dto

import com.kou.kouappapi.enums.Emotion
import java.time.LocalDate

data class CreateDiaryResponseDto(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
    val imageUrl: String? = null,
    val content: String? = null,
)
