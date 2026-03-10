package com.kou.usagiappapi.auth.service

import com.kou.usagiappapi.auth.exception.AuthLoginRequiredException
import com.kou.usagiappapi.auth.exception.AuthTokenExpiredException
import com.kou.usagiappapi.auth.exception.AuthUnauthorizedTokenAccessException
import com.kou.usagiappapi.auth.service.dto.RefreshTokenRequestDto
import com.kou.usagiappapi.auth.service.dto.RefreshTokenResponseDto
import com.kou.usagiappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.usagiappapi.auth.service.dto.SocialLoginResponseDto
import com.kou.usagiappapi.auth.social.SocialAuthStrategyFactory
import com.kou.usagiappapi.auth.social.SocialUserInfo
import com.kou.usagiappapi.entity.RefreshToken
import com.kou.usagiappapi.entity.User
import com.kou.usagiappapi.enums.UserStatus
import com.kou.usagiappapi.manager.RedisManager
import com.kou.usagiappapi.repository.RefreshTokenRepository
import com.kou.usagiappapi.repository.UserRepository
import com.kou.usagiappapi.security.AuthUser
import com.kou.usagiappapi.security.jwt.JwtTokenProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val socialAuthStrategyFactory: SocialAuthStrategyFactory,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val redisManager: RedisManager,
) {
    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    @Transactional
    fun socialLogin(requestDto: SocialLoginRequestDto): SocialLoginResponseDto {
        val strategy = socialAuthStrategyFactory.getStrategy(requestDto.provider)
        val socialUserInfo = strategy.authenticate(requestDto.token)

        val user =
            userRepository.findByProviderAndProviderIdAndStatus(
                provider = socialUserInfo.provider,
                providerId = socialUserInfo.providerId,
                status = UserStatus.ACTIVE,
            ) ?: createNewUser(socialUserInfo)
        // TODO deleteAllById 성능 체크 필요
        //  https://programmer-chocho.tistory.com/102
        refreshTokenRepository.deleteAllByUserId(user.id)

        val accessToken = jwtTokenProvider.generateAccessToken(user.id, user.email, user.role)
        val refreshToken = jwtTokenProvider.generateRefreshToken(user.id, user.email, user.role)

        refreshTokenRepository.save(
            RefreshToken(
                userId = user.id,
                tokenHash = refreshToken,
                expiresAt = jwtTokenProvider.getExpiration(refreshToken),
            ),
        )

        return SocialLoginResponseDto(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

    private fun createNewUser(socialUserInfo: SocialUserInfo): User =
        userRepository.save(
            User(
                name = socialUserInfo.name,
                email = socialUserInfo.email,
                provider = socialUserInfo.provider,
                providerId = socialUserInfo.providerId,
            ),
        )

    @Transactional
    fun refreshToken(requestDto: RefreshTokenRequestDto): RefreshTokenResponseDto {
        val savedRefreshToken =
            refreshTokenRepository.findByTokenHash(requestDto.refreshToken) ?: throw AuthTokenExpiredException()

        jwtTokenProvider.validateToken(savedRefreshToken.tokenHash)

        val authUser = jwtTokenProvider.getAuthUser(savedRefreshToken.tokenHash)
        val user = userRepository.findByIdOrNull(authUser.id) ?: throw AuthUnauthorizedTokenAccessException()

        val accessToken = jwtTokenProvider.generateAccessToken(user.id, user.email, user.role)

        val shouldRefreshToken =
            jwtTokenProvider.shouldRefreshToken(savedRefreshToken.tokenHash, 7)
        if (shouldRefreshToken) {
            val newRefreshToken =
                jwtTokenProvider.generateRefreshToken(user.id, user.email, user.role)
            refreshTokenRepository.save(
                RefreshToken(
                    userId = user.id,
                    tokenHash = newRefreshToken,
                    expiresAt = jwtTokenProvider.getExpiration(newRefreshToken),
                ),
            )

            return RefreshTokenResponseDto(
                accessToken = accessToken,
                refreshToken = newRefreshToken,
            )
        }

        return RefreshTokenResponseDto(
            accessToken = accessToken,
            refreshToken = requestDto.refreshToken,
        )
    }

    fun validateToken(
        authUser: AuthUser?,
        request: HttpServletRequest?,
    ) {
        val authHeader =
            request?.getHeader(HttpHeaders.AUTHORIZATION)
                ?: throw AuthLoginRequiredException()
        if (!authHeader.startsWith(BEARER_PREFIX)) {
            throw AuthUnauthorizedTokenAccessException()
        }

        authUser ?: throw AuthTokenExpiredException()
    }

    @Transactional
    fun logout(
        userId: Long,
        request: HttpServletRequest,
    ) {
        // TODO  @AccessToken 커스텀 어노테이션 구현
        val authHeader =
            request.getHeader(HttpHeaders.AUTHORIZATION)
                ?: throw AuthLoginRequiredException()
        if (!authHeader.startsWith(BEARER_PREFIX)) {
            throw AuthUnauthorizedTokenAccessException()
        }

        val accessToken = authHeader.replace(BEARER_PREFIX, "").trim()
        val remainingTime = jwtTokenProvider.getRemainingTime(accessToken)
        if (remainingTime > 0) {
            redisManager.setBlackList(accessToken, remainingTime)
        }

        // TODO deleteAllById 성능 체크 필요
        //  https://programmer-chocho.tistory.com/102
        refreshTokenRepository.deleteAllByUserId(userId)
    }
}
