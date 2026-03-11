package com.kou.usagiappapi.diary.service.dto

import com.kou.usagiappapi.diary.controller.dto.GetDiariesResponse
import com.kou.usagiappapi.diary.enums.Emotion
import java.time.LocalDate

data class GetDiariesResponseDto(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
)

fun List<GetDiariesResponseDto>.toResponse(): List<GetDiariesResponse> =
    map {
        GetDiariesResponse(
            diaryId = it.diaryId,
            date = it.date,
            emotion = it.emotion,
        )
    }
