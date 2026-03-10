package com.kou.usagiappapi.controller.dto

import com.kou.usagiappapi.enums.Emotion
import java.time.LocalDate

data class GetDiaryListResponse(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
)
