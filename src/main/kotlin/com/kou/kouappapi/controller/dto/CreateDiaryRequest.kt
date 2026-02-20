package com.kou.kouappapi.controller.dto

import com.kou.kouappapi.enums.Emotion
import com.kou.kouappapi.service.dto.CreateDiaryRequestDto
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

data class CreateDiaryRequest(
    val date: LocalDate,
    val emotion: Emotion,
    @RequestPart(required = false)
    val imageFile: MultipartFile? = null,
    val content: String? = null,
)

fun CreateDiaryRequest.toDto(): CreateDiaryRequestDto =
    CreateDiaryRequestDto(
        date = date,
        emotion = emotion,
        imageFile = imageFile,
        content = content,
    )
