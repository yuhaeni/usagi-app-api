package com.kou.usagiappapi.diary.controller.dto

import com.kou.usagiappapi.activityCategory.service.ActivityCategoryResponseDto
import com.kou.usagiappapi.enums.Emotion
import java.time.LocalDate

data class GetDiaryResponse(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
    val imageUrl: String? = null,
    val content: String? = null,
    val diaryActivityCategories: List<ActivityCategoryResponseDto> = emptyList(),
)
