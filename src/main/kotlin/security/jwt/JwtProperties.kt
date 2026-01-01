package security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    var secret: String = "",
    var accessTokenExpireTime: Long = 1800000,  // 30분
    var refreshTokenExpireTime: Long = 604800000  // 7일
)
