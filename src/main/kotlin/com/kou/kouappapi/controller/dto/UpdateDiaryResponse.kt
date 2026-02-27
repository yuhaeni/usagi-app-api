package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.enums.Emotion
import java.time.LocalDate

data class UpdateDiaryResponse(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
    val imageUrl: String? = null,
    val content: String? = null,
    val deleteImage: Boolean? = false,
)
