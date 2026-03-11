package com.kou.usagiappapi.diary.service.dto

import com.kou.usagiappapi.diary.enums.Emotion

data class UpdateDiaryRequestDto(
    val emotion: Emotion? = null,
    val content: String? = null,
    val deleteImage: Boolean? = null,
    val activityCategoryIds: List<Long>? = null,
)
