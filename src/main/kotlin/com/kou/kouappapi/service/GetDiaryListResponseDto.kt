package com.kou.kouappapi.service

import com.kou.kouappapi.controller.dto.GetDiaryListResponse
import com.kou.kouappapi.enums.Emotion
import java.time.LocalDate

data class GetDiaryListResponseDto(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
)

fun List<GetDiaryListResponseDto>.toResponse(): List<GetDiaryListResponse> =
    map {
        GetDiaryListResponse(
            diaryId = it.diaryId,
            date = it.date,
            emotion = it.emotion,
        )
    }
