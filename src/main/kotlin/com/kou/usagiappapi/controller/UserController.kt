package com.kou.usagiappapi.controller

import com.kou.usagiappapi.common.dto.ApiResponse
import com.kou.usagiappapi.controller.dto.GetUserProfileResponse
import com.kou.usagiappapi.controller.dto.UpdateUserProfileRequest
import com.kou.usagiappapi.controller.dto.UpdateUserProfileResponse
import com.kou.usagiappapi.controller.dto.toDto
import com.kou.usagiappapi.controller.dto.toResponse
import com.kou.usagiappapi.diary.controller.dto.toDto
import com.kou.usagiappapi.security.AuthUser
import com.kou.usagiappapi.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "🧑‍🧒‍🧒 사용자")
@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val service: UserService,
) {
    @Operation(summary = "유저 프로필 조회")
    @GetMapping("/me")
    fun getUserProfile(
        @AuthenticationPrincipal user: AuthUser,
    ): GetUserProfileResponse = service.getUserProfile(user.id).toResponse()

    @Operation(summary = "유저 프로필 수정")
    @PatchMapping("/me", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateUserProfile(
        @AuthenticationPrincipal user: AuthUser,
        @ModelAttribute request: UpdateUserProfileRequest,
    ): UpdateUserProfileResponse = service.updateUserProfile(user.id, request.toDto())

    @Operation(summary = "회원 탈퇴")
    @DeleteMapping("/me")
    fun withdrawUser(
        @AuthenticationPrincipal user: AuthUser,
        request: HttpServletRequest,
    ): ApiResponse<Unit> = ApiResponse.success(service.withdrawUser(user.id, request))
}
