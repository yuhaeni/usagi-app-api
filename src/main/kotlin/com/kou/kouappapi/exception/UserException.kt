package com.kou.kouappapi.exception

open class UserException(
    message: String,
) : RuntimeException(message)

class UserNotFoundException : UserException("사용자를 찾을 수 없습니다.")
