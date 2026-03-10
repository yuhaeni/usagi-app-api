package com.kou.usagiappapi.diary.controller.dto

import com.kou.usagiappapi.diary.service.dto.UpdateDiaryRequestDto
import com.kou.usagiappapi.enums.Emotion

data class UpdateDiaryRequest(
    val emotion: Emotion? = null,
    val content: String? = null,
    val deleteImage: Boolean? = null,
    val activityCategoryIdList: List<Long>? = null,
)

fun UpdateDiaryRequest.toDto() =
    UpdateDiaryRequestDto(
        emotion = emotion,
        content = content,
        deleteImage = deleteImage,
        activityCategoryIds = activityCategoryIdList,
    )
