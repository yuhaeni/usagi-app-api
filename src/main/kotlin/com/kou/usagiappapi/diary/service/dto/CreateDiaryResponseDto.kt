package com.kou.usagiappapi.diary.service.dto

import com.kou.usagiappapi.diary.controller.dto.CreateDiaryResponse
import com.kou.usagiappapi.enums.Emotion
import com.kou.usagiappapi.service.dto.ActivityCategoryResponseDto
import com.kou.usagiappapi.service.dto.toResponse
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
