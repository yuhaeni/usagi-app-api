package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.enums.Emotion
import java.time.LocalDate

data class GetDiaryListResponse(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
)
