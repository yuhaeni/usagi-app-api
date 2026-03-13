package com.kou.usagiappapi.auth.enums

import org.springframework.http.HttpStatus

enum class AuthError(
    val status: HttpStatus,
    val message: String,
) {
    AUTH_EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN, "이메일 인증이 완료되지 않았습니다."),
    AUTH_PROVIDER_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "지원하지 않는 인증 제공자입니다."),
    AUTH_INVALID_ID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 id 토큰입니다."),
    AUTH_GENERATE_TOKEN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "토큰 생성에 실패했습니다."),
    AUTH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 인증 토큰입니다."),
    AUTH_UNAUTHORIZED_TOKEN_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없는 토큰입니다."),
    AUTH_LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
}
