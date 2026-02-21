package com.kou.kouappapi.exception

import com.kou.kouappapi.common.dto.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        val fieldError = e.bindingResult.fieldErrors.firstOrNull()
        val errorMessage =
            when {
                fieldError == null -> "잘못된 요청입니다."
                fieldError.field == "date" && fieldError.code == "typeMismatch" -> "날짜는 'yyyy-MM-dd' 형식만 가능합니다."
                else -> fieldError.defaultMessage ?: "요청 값이 올바르지 않습니다."
            }

        return ResponseEntity
            .badRequest()
            .body(ApiResponse.failure(message = errorMessage))
    }
}
