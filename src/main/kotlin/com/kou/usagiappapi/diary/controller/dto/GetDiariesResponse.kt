package com.kou.usagiappapi.diary.controller.dto

import com.kou.usagiappapi.enums.Emotion
import java.time.LocalDate

data class GetDiariesResponse(
    val diaryId: Long,
    val date: LocalDate,
    val emotion: Emotion,
)
