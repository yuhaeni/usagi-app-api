package com.kou.usagiappapi.diary.service.dto

import com.kou.usagiappapi.activityCategory.service.dto.ActivityCategoryResponseDto
import com.kou.usagiappapi.diary.controller.dto.UpdateDiaryResponse
import com.kou.usagiappapi.diary.enums.Emotion
import java.time.LocalDate

data class UpdateDiaryResponseDto(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
    val imageUrl: String? = null,
    val content: String? = null,
    val diaryActivityCategories: List<ActivityCategoryResponseDto> = emptyList(),
)

fun UpdateDiaryResponseDto.toResponse(): UpdateDiaryResponse =
    UpdateDiaryResponse(
        diaryId = diaryId,
        date = date,
        emotion = emotion,
        imageUrl = imageUrl,
        content = content,
        diaryActivityCategories = diaryActivityCategories,
    )
