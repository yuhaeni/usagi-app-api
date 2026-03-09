package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.enums.Emotion
import com.kou.kouappapi.service.dto.CreateDiaryRequestDto
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

data class CreateDiaryRequest(
    @field:PastOrPresent(message = "미래의 날짜로 일기를 작성할 수 없습니다.")
    val date: LocalDate,
    val emotion: Emotion,
    @RequestPart(required = false)
    val imageFile: MultipartFile? = null,
    @field:Size(max = 300, message = "내용은 최대 300자까지 작성 가능합니다.")
    val content: String? = null,
    val activityCategoryIds: List<Long> = emptyList(),
)

fun CreateDiaryRequest.toDto(): CreateDiaryRequestDto =
    CreateDiaryRequestDto(
        date = date,
        emotion = emotion,
        imageFile = imageFile,
        content = content,
        activityCategoryIds = activityCategoryIds,
    )
