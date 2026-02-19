package com.kou.kouappapi.controller

import com.kou.kouappapi.controller.dto.CreateDiaryRequest
import com.kou.kouappapi.controller.dto.toDto
import com.kou.kouappapi.security.AuthUser
import com.kou.kouappapi.service.DiaryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "📔 일기")
@RestController
@RequestMapping("/api/v1/diary")
class DiaryController(
    val service: DiaryService,
) {
    @Operation(summary = "일기 작성")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createDiary(
        @AuthenticationPrincipal user: AuthUser,
        @ModelAttribute request: CreateDiaryRequest,
    ) = service.createDiary(user.id, request.toDto())
}
