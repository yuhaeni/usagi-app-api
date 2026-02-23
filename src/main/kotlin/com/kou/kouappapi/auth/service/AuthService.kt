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
import com.kou.kouappapi.enums.Role
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
) {
    @Transactional
    fun socialLogin(requestDto: SocialLoginRequestDto): SocialLoginResponseDto {
        val strategy = socialAuthStrategyFactory.getStrategy(requestDto.provider)
        val socialUserInfo = strategy.authenticate(requestDto.token)

        val user =
            userRepository.findByProviderAndProviderId(socialUserInfo.provider, socialUserInfo.providerId)
                ?: createNewUser(socialUserInfo)

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

    @Transactional
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
        jwtTokenProvider.validateToken(requestDto.refreshToken)

        refreshTokenRepository.findByTokenHash(requestDto.refreshToken) ?: throw AuthTokenExpiredException()

        val authUser = jwtTokenProvider.getAuthUser(requestDto.refreshToken)
        userRepository.findByIdOrNull(authUser.id) ?: throw AuthUnauthorizedTokenAccessException()

        val accessToken = jwtTokenProvider.generateAccessToken(authUser.id, authUser.email, Role.valueOf(authUser.role))

        val shouldRefreshToken =
            jwtTokenProvider.shouldRefreshToken(requestDto.refreshToken, 7)
        if (shouldRefreshToken) {
            val newRefreshToken =
                jwtTokenProvider.generateRefreshToken(authUser.id, authUser.email, Role.valueOf(authUser.role))
            refreshTokenRepository.save(
                RefreshToken(
                    userId = authUser.id,
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
        request?.getHeader(HttpHeaders.AUTHORIZATION)
            ?: throw AuthLoginRequiredException()
        authUser ?: throw AuthTokenExpiredException()
    }
}
