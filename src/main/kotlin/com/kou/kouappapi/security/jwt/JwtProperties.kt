package com.kou.kouappapi.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    var secret: String = "",
    var accessTokenExpireDuration: Long = 0L, // 30분
    var refreshTokenExpireDuration: Long = 0L, // 14일
)
