package com.kou.usagiappapi.exception

import org.springframework.http.HttpStatus

open class GlobalException(
    val status: HttpStatus,
    message: String,
) : RuntimeException(message)
