package com.kou.usagiappapi.controller

import com.kou.usagiappapi.common.dto.ApiResponse
import com.kou.usagiappapi.controller.dto.GetActivityCategoryListResponse
import com.kou.usagiappapi.security.AuthUser
import com.kou.usagiappapi.service.ActivityCategoryService
import com.kou.usagiappapi.service.dto.toResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "🎡 활동 카테고리")
@RestController
@RequestMapping("/api/v1/activity-category")
class ActivityCategoryController(
    val service: ActivityCategoryService,
) {
    @Operation(summary = "활동 카테고리 목록 조회", description = "MVP 버전에서는 기본 활동 카테고리 목록만 조회. (추후에 사용자 커스텀 추가)")
    @GetMapping("/list")
    fun getActivityCategoryList(
        @AuthenticationPrincipal user: AuthUser,
    ): ApiResponse<List<GetActivityCategoryListResponse>> =
        ApiResponse.success(service.getActivityCategoryList(user.id).toResponse())
}
