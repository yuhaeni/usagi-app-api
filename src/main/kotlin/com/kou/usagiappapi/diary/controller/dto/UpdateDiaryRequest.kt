package com.kou.usagiappapi.diary.controller.dto

import com.kou.usagiappapi.diary.enums.Emotion
import com.kou.usagiappapi.diary.service.dto.UpdateDiaryRequestDto

data class UpdateDiaryRequest(
    val emotion: Emotion? = null,
    val content: String? = null,
    val deleteImage: Boolean? = null,
    val activityCategoryIds: List<Long>? = null,
)

fun UpdateDiaryRequest.toDto() =
    UpdateDiaryRequestDto(
        emotion = emotion,
        content = content,
        deleteImage = deleteImage,
        activityCategoryIds = activityCategoryIds,
    )
