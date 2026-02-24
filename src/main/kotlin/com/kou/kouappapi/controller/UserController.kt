package com.kou.kouappapi.controller

import com.kou.kouappapi.controller.dto.GetUserProfileResponse
import com.kou.kouappapi.controller.dto.ModifyUserProfileRequest
import com.kou.kouappapi.controller.dto.ModifyUserProfileResponse
import com.kou.kouappapi.controller.dto.toDto
import com.kou.kouappapi.controller.dto.toResponse
import com.kou.kouappapi.security.AuthUser
import com.kou.kouappapi.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PutMapping
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
    @PutMapping("/me", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun modifyUserProfile(
        @AuthenticationPrincipal user: AuthUser,
        @ModelAttribute request: ModifyUserProfileRequest,
    ): ModifyUserProfileResponse = service.modifyUserProfile(user.id, request.toDto())
}
