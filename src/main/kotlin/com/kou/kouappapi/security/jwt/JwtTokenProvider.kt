package com.kou.kouappapi.security.jwt

import com.kou.kouappapi.enums.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties,
) {
    companion object {
        // TODO token type enum으로?
        private const val TOKEN_TYPE_ACCESS = "ACCESS"
        private const val TOKEN_TYPE_REFRESH = "REFRESH"
        private const val TOKEN_TYPE_CLAIM = "type"
        private const val EMAIL_CLAIM = "email"
        private const val ROLE_CLAIM = "role"
    }

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val secretKey: SecretKey by lazy {
        println(jwtProperties.secret)
        Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
    }

    /**
     * Access Token 생성
     */
    fun generateAccessToken(
        userId: Long?,
        email: String,
        role: Role,
    ): String =
        generateToken(
            userId = userId,
            email = email,
            role = role,
            expireTime = jwtProperties.accessTokenExpireTime,
            tokenType = TOKEN_TYPE_ACCESS,
        )

    /**
     * Refresh Token 생성
     */
    fun generateRefreshToken(
        userId: Long?,
        email: String,
        role: Role,
    ): String =
        generateToken(
            userId = userId,
            email = email,
            role = role,
            expireTime = jwtProperties.refreshTokenExpireTime,
            tokenType = TOKEN_TYPE_REFRESH,
        )

    /**
     * 토큰 생성 (공통 로직)
     */
    private fun generateToken(
        userId: Long?,
        email: String,
        role: Role,
        expireTime: Long,
        tokenType: String,
    ): String {
        val now = Date()
        val expireTime = Date(now.time + expireTime)

        return Jwts
            .builder()
            .subject(userId.toString())
            .claim(EMAIL_CLAIM, email)
            .claim(TOKEN_TYPE_CLAIM, tokenType)
            .claim(ROLE_CLAIM, role)
            .issuedAt(now)
            .expiration(expireTime)
            .signWith(secretKey)
            .compact()
    }

    /**
     * 토큰 검증
     */
    fun validateToken(token: String): Boolean =
        runCatching { parseToken(token) }
            .onFailure(::logValidationError)
            .isSuccess

    /**
     * 검증 에러 로깅
     */
    private fun logValidationError(e: Throwable) {
        val message =
            when (e) {
                is SecurityException -> "Invalid JWT signature"
                is MalformedJwtException -> "Invalid JWT token"
                is ExpiredJwtException -> "Expired JWT token"
                is UnsupportedJwtException -> "Unsupported JWT token"
                is IllegalArgumentException -> "JWT claims string is empty"
                else -> "JWT token validation error"
            }
        logger.error("$message: ${e.message}")
    }

    /**
     * 토큰에서 userId 추출
     */
    fun getUserIdFromToken(token: String): Long = getClaims(token).subject.toLong()

    /**
     * 토큰에서 email 추출
     */
    fun getEmailFromToken(token: String): String = getClaims(token).get(EMAIL_CLAIM, String::class.java)

    /**
     * 토큰에서 role 추출
     */
    fun getRoleFromToken(token: String): String = getClaims(token).get(ROLE_CLAIM, String::class.java)

    /**
     * 토큰에서 모든 Claims 추출
     */
    fun getClaims(token: String): Claims = parseToken(token).payload

    /**
     * 토큰 만료 시간 확인
     */
    fun getExpiration(token: String): Date = parseToken(token).payload.expiration

    /**
     * 토큰 파싱
     * */
    private fun parseToken(token: String): Jws<Claims> =
        Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
}
