package com.kou.kouappapi.exception

import com.kou.kouappapi.common.dto.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException::class)
    fun handleGlobalException(e: GlobalException): ResponseEntity<ApiResponse<Unit>> {
        val errorMessage = e.message ?: "Server error"
        return ResponseEntity
            .status(e.status)
            .body(ApiResponse.failure(message = errorMessage))
    }
}
