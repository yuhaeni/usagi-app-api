package com.kou.usagiappapi.diary.service.dto

import com.kou.usagiappapi.diary.controller.dto.GetDiaryResponse
import com.kou.usagiappapi.enums.Emotion
import com.kou.usagiappapi.service.dto.ActivityCategoryResponseDto
import java.time.LocalDate

data class GetDiaryResponseDto(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
    val imageUrl: String? = null,
    val content: String? = null,
    val diaryActivityCategories: List<ActivityCategoryResponseDto> = emptyList(),
)

fun GetDiaryResponseDto.toResponse(): GetDiaryResponse =
    GetDiaryResponse(
        diaryId = diaryId,
        date = date,
        emotion = emotion,
        imageUrl = imageUrl,
        content = content,
        diaryActivityCategories = diaryActivityCategories,
    )
