package com.kou.usagiappapi.diary.controller.dto

import com.kou.usagiappapi.diary.enums.Emotion
import com.kou.usagiappapi.diary.service.dto.UpdateDiaryRequestDto
import jakarta.validation.constraints.Size

data class UpdateDiaryRequest(
    val emotion: Emotion? = null,
    @field:Size(max = 300, message = "내용은 최대 300자까지 작성 가능합니다.")
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
