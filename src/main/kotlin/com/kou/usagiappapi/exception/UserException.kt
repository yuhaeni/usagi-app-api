package com.kou.usagiappapi.exception

import org.springframework.http.HttpStatus

open class UserException(
    status: HttpStatus,
    message: String,
) : GlobalException(status, message)

class UserNotFoundException : UserException(status = HttpStatus.NOT_FOUND, message = "사용자를 찾을 수 없습니다.")
