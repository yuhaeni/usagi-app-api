package com.kou.kouappapi.controller

import com.kou.kouappapi.controller.dto.CreateDiaryRequest
import com.kou.kouappapi.controller.dto.CreateDiaryResponse
import com.kou.kouappapi.controller.dto.GetDiaryResponse
import com.kou.kouappapi.controller.dto.toDto
import com.kou.kouappapi.security.AuthUser
import com.kou.kouappapi.service.DiaryService
import com.kou.kouappapi.service.dto.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
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
        @Valid @ModelAttribute request: CreateDiaryRequest,
    ): CreateDiaryResponse = service.createDiary(user.id, request.toDto()).toResponse()

    @Operation(summary = "일기 상세 조회")
    @GetMapping("{diaryId}")
    fun getDiary(
        @AuthenticationPrincipal user: AuthUser,
        @PathVariable("diaryId") diaryId: Long,
    ): GetDiaryResponse = service.getDiary(user.id, diaryId).toResponse()
}
