package com.kou.usagiappapi.activityCategory.controller

import com.kou.usagiappapi.activityCategory.controller.dto.GetActivityCategoriesResponse
import com.kou.usagiappapi.activityCategory.service.ActivityCategoryService
import com.kou.usagiappapi.activityCategory.service.dto.toResponse
import com.kou.usagiappapi.global.security.AuthUser
import com.kou.usagiappapi.shared.dto.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "🎡 활동 카테고리")
@RestController
@RequestMapping("/api/v1/activity-categories")
class ActivityCategoryController(
    private val service: ActivityCategoryService,
) {
    @Operation(summary = "활동 카테고리 목록 조회", description = "MVP 버전에서는 기본 활동 카테고리 목록만 조회. (추후에 사용자 커스텀 추가)")
    @GetMapping
    fun getActivityCategories(
        @AuthenticationPrincipal user: AuthUser,
    ): ApiResponse<List<GetActivityCategoriesResponse>> =
        ApiResponse.success(service.getActivityCategories(user.id).toResponse())
}
