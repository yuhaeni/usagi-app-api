package com.kou.kouappapi.auth.controller

import com.kou.kouappapi.auth.controller.dto.SocialLoginRequest
import com.kou.kouappapi.auth.controller.dto.SocialLoginResponse
import com.kou.kouappapi.auth.controller.dto.toDto
import com.kou.kouappapi.auth.controller.dto.toResponse
import com.kou.kouappapi.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService,
) {
    @PostMapping("/social/login")
    fun socialLogin(
        @RequestBody @Valid request: SocialLoginRequest,
    ): ResponseEntity<SocialLoginResponse> =
        ResponseEntity.ok(
            authService.socialLogin(request.toDto()).toResponse(),
        )
}
