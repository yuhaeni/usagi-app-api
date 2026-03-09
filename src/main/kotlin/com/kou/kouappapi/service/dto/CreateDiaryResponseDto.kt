package com.kou.kouappapi.service.dto

import com.kou.kouappapi.controller.dto.CreateDiaryResponse
import com.kou.kouappapi.enums.Emotion
import java.time.LocalDate

data class CreateDiaryResponseDto(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
    val imageUrl: String? = null,
    val content: String? = null,
    val diaryActivityCategories: List<ActivityCategoryResponseDto> = emptyList(),
)

fun CreateDiaryResponseDto.toResponse(): CreateDiaryResponse =
    CreateDiaryResponse(
        diaryId = diaryId,
        date = date,
        emotion = emotion,
        imageUrl = imageUrl,
        content = content,
        diaryActivityCategories = diaryActivityCategories.toResponse(),
    )
