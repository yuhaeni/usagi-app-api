package com.kou.kouappapi.service.dto

import com.kou.kouappapi.controller.dto.GetDiaryResponse
import com.kou.kouappapi.enums.Emotion
import java.time.LocalDate

data class GetDiaryResponseDto(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
    val imageUrl: String? = null,
    val content: String? = null,
    val activityCategoryList: List<ActivityCategoryResponseDto> = emptyList(),
)

fun GetDiaryResponseDto.toResponse(): GetDiaryResponse =
    GetDiaryResponse(
        diaryId = diaryId,
        date = date,
        emotion = emotion,
        imageUrl = imageUrl,
        content = content,
        activityCategoryList = activityCategoryList,
    )
