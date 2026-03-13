package com.kou.usagiappapi.global.security

data class AuthUser(
    val id: Long,
    val email: String,
    val role: String,
)
