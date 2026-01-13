package com.kou.kouappapi.auth.service

import com.kou.kouappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.kouappapi.auth.service.dto.SocialLoginResponseDto
import com.kou.kouappapi.auth.social.SocialAuthStrategyFactory
import com.kou.kouappapi.auth.social.SocialUserInfo
import com.kou.kouappapi.entity.User
import com.kou.kouappapi.entity.toResponseDto
import com.kou.kouappapi.enums.LoginNextStep
import com.kou.kouappapi.repository.UserRepository
import com.kou.kouappapi.security.jwt.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthService(
    private val socialAuthStrategyFactory: SocialAuthStrategyFactory,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository,
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

        return SocialLoginResponseDto(
            accessToken = accessToken,
            refreshToken = refreshToken,
            nextStep = LoginNextStep.PROFILE_SETUP,
            user = user.toResponseDto(),
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
}
