package com.kou.kouappapi.auth.controller

import com.kou.kouappapi.auth.controller.dto.RefreshTokenRequest
import com.kou.kouappapi.auth.controller.dto.RefreshTokenResponse
import com.kou.kouappapi.auth.controller.dto.SocialLoginRequest
import com.kou.kouappapi.auth.controller.dto.SocialLoginResponse
import com.kou.kouappapi.auth.controller.dto.toDto
import com.kou.kouappapi.auth.controller.dto.toResponse
import com.kou.kouappapi.auth.service.AuthService
import com.kou.kouappapi.common.dto.ApiResponse
import com.kou.kouappapi.security.AuthUser
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "🔐 인증")
@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val service: AuthService,
) {
    @Operation(summary = "소셜 로그인", description = "소셜 로그인을 통해 로그인 및 회원가입 처리")
    @PostMapping("/social/login")
    fun socialLogin(
        @RequestBody request: SocialLoginRequest,
    ): ResponseEntity<SocialLoginResponse> =
        ResponseEntity.ok(
            service.socialLogin(request.toDto()).toResponse(),
        )

    @Operation(
        summary = "토큰 재발급",
        description = "access token 재발급 (refresh token의 만료일이 다가오는 경우, refresh token도 재발급)",
    )
    @PostMapping("/refresh/token")
    fun refreshToken(
        @RequestBody request: RefreshTokenRequest,
    ): ApiResponse<RefreshTokenResponse> = ApiResponse.success(service.refreshToken(request.toDto()).toResponse())

    @Operation(
        summary = "토큰 검증",
        description = "유효한 access token 검증",
    )
    @PostMapping("/validate/token")
    fun validateToken(
        @AuthenticationPrincipal authUser: AuthUser?,
        request: HttpServletRequest,
    ): ApiResponse<Unit> = ApiResponse.success(service.validateToken(authUser, request))
}
