package com.kou.kouappapi.service.dto

import com.kou.kouappapi.enums.Emotion

data class UpdateDiaryRequestDto(
    val emotion: Emotion? = null,
    val content: String? = null,
    val deleteImage: Boolean? = null,
    val activityCategoryIdList: List<Long>? = null,
)
