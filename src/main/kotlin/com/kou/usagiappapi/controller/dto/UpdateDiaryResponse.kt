package com.kou.usagiappapi.controller.dto

import com.kou.usagiappapi.enums.Emotion
import com.kou.usagiappapi.service.dto.ActivityCategoryResponseDto
import java.time.LocalDate

data class UpdateDiaryResponse(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
    val imageUrl: String? = null,
    val content: String? = null,
    val diaryActivityCategories: List<ActivityCategoryResponseDto> = emptyList(),
)
