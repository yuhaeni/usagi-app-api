package com.kou.usagiappapi.diary.service.dto

import com.kou.usagiappapi.diary.enums.Emotion
import java.time.LocalDate

data class CreateDiaryRequestDto(
    val date: LocalDate,
    val emotion: Emotion,
    val content: String? = null,
    val activityCategoryIds: List<Long> = emptyList(), // 생성 시 미선택 = 빈 리스트
)
