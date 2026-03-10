package com.kou.usagiappapi.service.dto

import com.kou.usagiappapi.enums.Emotion

data class UpdateDiaryRequestDto(
    val emotion: Emotion? = null,
    val content: String? = null,
    val deleteImage: Boolean? = null,
    val activityCategoryIdList: List<Long>? = null,
)
