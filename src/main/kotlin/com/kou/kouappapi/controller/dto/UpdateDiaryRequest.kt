package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.enums.Emotion
import com.kou.kouappapi.service.dto.UpdateDiaryRequestDto

data class UpdateDiaryRequest(
    val emotion: Emotion? = null,
    val content: String? = null,
    val deleteImage: Boolean? = null,
    val activityCategoryIdList: List<Long>? = emptyList(),
)

fun UpdateDiaryRequest.toDto() =
    UpdateDiaryRequestDto(
        emotion = emotion,
        content = content,
        deleteImage = deleteImage,
        activityCategoryIdList = activityCategoryIdList,
    )
