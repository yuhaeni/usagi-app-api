package com.kou.usagiappapi.diary.controller

import com.kou.usagiappapi.diary.controller.dto.CreateDiaryRequest
import com.kou.usagiappapi.diary.controller.dto.CreateDiaryResponse
import com.kou.usagiappapi.diary.controller.dto.GetDiariesResponse
import com.kou.usagiappapi.diary.controller.dto.GetDiaryResponse
import com.kou.usagiappapi.diary.controller.dto.UpdateDiaryRequest
import com.kou.usagiappapi.diary.controller.dto.UpdateDiaryResponse
import com.kou.usagiappapi.diary.controller.dto.toDto
import com.kou.usagiappapi.diary.service.DiaryService
import com.kou.usagiappapi.diary.service.dto.UpdateDiaryRequestDto
import com.kou.usagiappapi.diary.service.dto.toResponse
import com.kou.usagiappapi.global.security.AuthUser
import com.kou.usagiappapi.shared.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@Tag(name = "📔 일기")
@RestController
@RequestMapping("/api/v1/diaries")
class DiaryController(
    private val service: DiaryService,
) {
    @Operation(summary = "일기 목록 조회", description = "요청 날짜가 없는 경우 현재 월 기준으로 조회")
    @GetMapping
    fun getDiaries(
        @AuthenticationPrincipal user: AuthUser,
        @RequestParam startDate: LocalDate?,
        @RequestParam endDate: LocalDate?,
    ): ApiResponse<List<GetDiariesResponse>> =
        ApiResponse.success(
            service.getDiaries(userId = user.id, startDate = startDate, endDate = endDate).toResponse(),
        )

    @Operation(summary = "일기 상세 조회")
    @GetMapping("/{id}")
    fun getDiary(
        @AuthenticationPrincipal user: AuthUser,
        @PathVariable("id") id: Long,
    ): ApiResponse<GetDiaryResponse> = ApiResponse.success(service.getDiary(user.id, id).toResponse())

    @Operation(summary = "일기 작성")
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun createDiary(
        @AuthenticationPrincipal user: AuthUser,
        @Valid
        @Parameter(
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)],
        )
        @RequestPart("data") request: CreateDiaryRequest,
        @RequestPart("imageFile", required = false) imageFile: MultipartFile?,
    ): ApiResponse<CreateDiaryResponse> =
        ApiResponse.success(service.createDiary(user.id, request.toDto(), imageFile).toResponse())

    @Operation(summary = "일기 수정")
    @PatchMapping("/{id}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateDiary(
        @AuthenticationPrincipal user: AuthUser,
        @PathVariable("id") id: Long,
        @Valid
        @RequestPart("data", required = false)
        @Parameter(
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)],
        )
        request: UpdateDiaryRequest?,
        @RequestPart("imageFile", required = false) imageFile: MultipartFile?,
    ): ApiResponse<UpdateDiaryResponse> =
        ApiResponse.success(
            service
                .updateDiary(
                    userId = user.id,
                    diaryId = id,
                    requestDto = request?.toDto() ?: UpdateDiaryRequestDto(),
                    imageFile = imageFile,
                ).toResponse(),
        )

    @Operation(summary = "일기 삭제")
    @DeleteMapping("/{id}")
    fun deleteDiary(
        @AuthenticationPrincipal user: AuthUser,
        @PathVariable("id") id: Long,
    ): ApiResponse<Unit> {
        service.deleteDiary(userId = user.id, diaryId = id)
        return ApiResponse.success(Unit, "일기가 삭제되었습니다.")
    }
}
