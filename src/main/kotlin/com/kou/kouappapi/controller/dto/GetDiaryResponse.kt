package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.enums.Emotion
import com.kou.kouappapi.service.dto.ActivityCategoryResponseDto
import java.time.LocalDate

data class GetDiaryResponse(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
    val imageUrl: String? = null,
    val content: String? = null,
    val diaryActivityCategories: List<ActivityCategoryResponseDto> = emptyList(),
)
