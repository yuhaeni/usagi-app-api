package com.kou.kouappapi.controller

import com.kou.kouappapi.common.dto.ApiResponse
import com.kou.kouappapi.controller.dto.CreateDiaryRequest
import com.kou.kouappapi.controller.dto.CreateDiaryResponse
import com.kou.kouappapi.controller.dto.GetDiaryListResponse
import com.kou.kouappapi.controller.dto.GetDiaryResponse
import com.kou.kouappapi.controller.dto.UpdateDiaryRequest
import com.kou.kouappapi.controller.dto.UpdateDiaryResponse
import com.kou.kouappapi.controller.dto.toDto
import com.kou.kouappapi.security.AuthUser
import com.kou.kouappapi.service.DiaryService
import com.kou.kouappapi.service.dto.toResponse
import com.kou.kouappapi.service.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

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
    ): ApiResponse<CreateDiaryResponse> =
        ApiResponse.success(service.createDiary(user.id, request.toDto()).toResponse())

    @Operation(summary = "일기 상세 조회")
    @GetMapping("{diaryId}")
    fun getDiary(
        @AuthenticationPrincipal user: AuthUser,
        @PathVariable("diaryId") diaryId: Long,
    ): ApiResponse<GetDiaryResponse> = ApiResponse.success(service.getDiary(user.id, diaryId).toResponse())

    @Operation(summary = "일기 목록 조회")
    @GetMapping("/list")
    fun getDiaryList(
        @AuthenticationPrincipal user: AuthUser,
        @RequestParam startDate: LocalDate?,
        @RequestParam endDate: LocalDate?,
    ): ApiResponse<List<GetDiaryListResponse>> =
        ApiResponse.success(
            service.getDiaryList(userId = user.id, startDate = startDate, endDate = endDate).toResponse(),
        )

    @Operation(summary = "일기 수정")
    @PutMapping("{diaryId}")
    fun updateDiary(
        @AuthenticationPrincipal user: AuthUser,
        @PathVariable("diaryId") diaryId: Long,
        @RequestBody request: UpdateDiaryRequest,
    ): ApiResponse<UpdateDiaryResponse> =
        ApiResponse.success(service.updateDiary(user.id, diaryId, request.toDto()).toResponse())

    @Operation(summary = "일기 삭제")
    @DeleteMapping("{diaryId}")
    fun deleteDiary(
        @AuthenticationPrincipal user: AuthUser,
        @PathVariable("diaryId") diaryId: Long,
    ): ApiResponse<Unit> {
        service.deleteDiary(userId = user.id, diaryId = diaryId)
        return ApiResponse.success(Unit, "일기가 삭제되었습니다.")
    }
}
