package com.kou.usagiappapi.controller.dto

import com.kou.usagiappapi.enums.Emotion
import com.kou.usagiappapi.service.dto.UpdateDiaryRequestDto

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
        activityCategoryIdList = activityCategoryIdList,
    )
