package com.kou.kouappapi.service.dto

import com.kou.kouappapi.controller.dto.UpdateDiaryResponse
import com.kou.kouappapi.enums.Emotion
import java.time.LocalDate

data class UpdateDiaryResponseDto(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
    val imageUrl: String? = null,
    val content: String? = null,
    val activityCategoryList: List<ActivityCategoryResponseDto> = emptyList(),
)

fun UpdateDiaryResponseDto.toResponse(): UpdateDiaryResponse =
    UpdateDiaryResponse(
        diaryId = diaryId,
        date = date,
        emotion = emotion,
        imageUrl = imageUrl,
        content = content,
        activityCategoryList = activityCategoryList,
    )
