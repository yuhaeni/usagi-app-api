package com.kou.kouappapi.auth.service

import com.kou.kouappapi.auth.exception.AuthLoginRequiredException
import com.kou.kouappapi.auth.exception.AuthTokenExpiredException
import com.kou.kouappapi.auth.exception.AuthUnauthorizedTokenAccessException
import com.kou.kouappapi.auth.service.dto.RefreshTokenRequestDto
import com.kou.kouappapi.auth.service.dto.RefreshTokenResponseDto
import com.kou.kouappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.kouappapi.auth.service.dto.SocialLoginResponseDto
import com.kou.kouappapi.auth.social.SocialAuthStrategyFactory
import com.kou.kouappapi.auth.social.SocialUserInfo
import com.kou.kouappapi.entity.RefreshToken
import com.kou.kouappapi.entity.User
import com.kou.kouappapi.manager.RedisManager
import com.kou.kouappapi.repository.RefreshTokenRepository
import com.kou.kouappapi.repository.UserRepository
import com.kou.kouappapi.security.AuthUser
import com.kou.kouappapi.security.jwt.JwtTokenProvider
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
    private val redisManger: RedisManager,
) {
    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    @Transactional
    fun socialLogin(requestDto: SocialLoginRequestDto): SocialLoginResponseDto {
        val strategy = socialAuthStrategyFactory.getStrategy(requestDto.provider)
        val socialUserInfo = strategy.authenticate(requestDto.token)

        val user =
            userRepository.findByProviderAndProviderId(socialUserInfo.provider, socialUserInfo.providerId)
                ?: createNewUser(socialUserInfo)
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
        val authHeader =
            request.getHeader(HttpHeaders.AUTHORIZATION)
                ?: throw AuthLoginRequiredException()
        if (!authHeader.startsWith(BEARER_PREFIX)) {
            throw AuthUnauthorizedTokenAccessException()
        }

        val accessToken = authHeader.replace(BEARER_PREFIX, "").trim()
        val remainingTime = jwtTokenProvider.getRemainingTime(accessToken)
        if (remainingTime > 0) {
            redisManger.setBlackList(accessToken, remainingTime)
        }

        // TODO deleteAllById 성능 체크 필요
        //  https://programmer-chocho.tistory.com/102
        refreshTokenRepository.deleteAllByUserId(userId)
    }
}
