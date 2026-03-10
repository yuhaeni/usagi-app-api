package com.kou.usagiappapi.diary.controller.dto

import com.kou.usagiappapi.controller.dto.ActivityCategoryResponse
import com.kou.usagiappapi.enums.Emotion
import java.time.LocalDate

data class CreateDiaryResponse(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
    val imageUrl: String? = null,
    val content: String? = null,
    val diaryActivityCategories: List<ActivityCategoryResponse> = emptyList(),
)
