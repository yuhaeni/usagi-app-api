package com.kou.kouappapi.exception

open class UserException(
    message: String,
) : RuntimeException(message)

class UserNotFoundException : UserException("사용자를 찾을 수 없습니다.")

class UserAlreadyProfileCompleteException : UserException("이미 프로필 설정 완료한 사용자 입니다.")
