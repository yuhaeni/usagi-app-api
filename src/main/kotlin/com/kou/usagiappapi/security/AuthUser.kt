package com.kou.usagiappapi.security

data class AuthUser(
    val id: Long,
    val email: String,
    val role: String,
)
