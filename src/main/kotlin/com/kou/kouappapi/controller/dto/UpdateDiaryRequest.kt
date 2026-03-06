package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.enums.Emotion
import com.kou.kouappapi.service.dto.UpdateDiaryRequestDto
import org.springframework.web.multipart.MultipartFile

data class UpdateDiaryRequest(
    val emotion: Emotion? = null,
    val imageFile: MultipartFile? = null,
    val content: String? = null,
    val deleteImage: Boolean? = null,
    val activityCategoryIdList: List<Long>? = emptyList(),
)

fun UpdateDiaryRequest.toDto() =
    UpdateDiaryRequestDto(
        emotion = emotion,
        imageFile = imageFile,
        content = content,
        deleteImage = deleteImage,
        activityCategoryIdList = activityCategoryIdList,
    )
