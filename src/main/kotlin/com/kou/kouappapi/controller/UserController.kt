package com.kou.kouappapi.controller

import com.kou.kouappapi.controller.dto.GetUserProfileResponse
import com.kou.kouappapi.controller.dto.toResponse
import com.kou.kouappapi.security.AuthUser
import com.kou.kouappapi.service.UserService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}
