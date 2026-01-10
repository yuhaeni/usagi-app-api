package com.kou.kouappapi.controller

import com.kou.kouappapi.auth.controller.dto.CompleteProfileRequest
import com.kou.kouappapi.auth.controller.dto.toDto
import com.kou.kouappapi.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val service: UserService,
) {
    @PostMapping("/me/profile/complete")
    fun completeProfile(request: CompleteProfileRequest) = service.completeProfile(request.toDto())
}
