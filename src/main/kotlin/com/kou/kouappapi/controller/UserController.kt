package com.kou.kouappapi.controller

import com.kou.kouappapi.controller.dto.CompleteProfileRequest
import com.kou.kouappapi.controller.dto.CompleteProfileResponse
import com.kou.kouappapi.controller.dto.toDto
import com.kou.kouappapi.controller.dto.toResponse
import com.kou.kouappapi.security.AuthUser
import com.kou.kouappapi.service.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val service: UserService,
) {
    @PostMapping("/me/profile/complete")
    fun completeProfile(
        @AuthenticationPrincipal user: AuthUser,
        @RequestBody request: CompleteProfileRequest,
    ): CompleteProfileResponse = service.completeProfile(user.id, request.toDto()).toResponse()
}
