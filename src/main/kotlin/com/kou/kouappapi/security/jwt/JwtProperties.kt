package com.kou.kouappapi.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    var secret: String = "",
    var accessTokenExpireDuration: Long = 1800000, // 30분
    var refreshTokenExpireDuration: Long = 1209600000, // 14일
)
