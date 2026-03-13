package com.kou.usagiappapi.service

import com.kou.usagiappapi.IntegrationTestSupport
import com.kou.usagiappapi.auth.entity.RefreshToken
import com.kou.usagiappapi.auth.exception.AuthInvalidIdTokenException
import com.kou.usagiappapi.auth.repository.RefreshTokenRepository
import com.kou.usagiappapi.auth.service.AuthService
import com.kou.usagiappapi.auth.service.dto.RefreshTokenRequestDto
import com.kou.usagiappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.usagiappapi.global.security.jwt.JwtTokenProvider
import com.kou.usagiappapi.user.entity.User
import com.kou.usagiappapi.user.enums.SocialProvider
import com.kou.usagiappapi.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.Tags
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldNotBe
import org.springframework.transaction.annotation.Transactional
import java.util.Date

@Tags("local-only")
@Transactional
class AuthServiceTest(
    private val authService: AuthService,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val jwtTokenProvider: JwtTokenProvider,
) : IntegrationTestSupport(
        {
            describe("google 소셜 로그인") {
                context("유효한 google id 토큰이 주어지면") {
                    it("로그인 토큰을 발행하고 refresh token을 DB에 저장한다") {
                        val responseSocialLogin =
                            authService.socialLogin(
                                SocialLoginRequestDto(
                                    provider = SocialProvider.GOOGLE,
                                    token = "",
                                ),
                            )

                        responseSocialLogin shouldNotBe null

                        val savedToken = refreshTokenRepository.findByTokenHash(responseSocialLogin.refreshToken)
                        savedToken shouldNotBe null
                        savedToken?.expiresAt?.shouldBeGreaterThan(Date())
                    }
                }
                context("유효하지 않은 google id 토큰이 주어지면") {
                    it("유효하지 않은 토큰 예외가 발생한다.") {
                        shouldThrow<AuthInvalidIdTokenException> {
                            authService.socialLogin(
                                SocialLoginRequestDto(
                                    provider = SocialProvider.GOOGLE,
                                    token = "test.test.test.test.test",
                                ),
                            )
                        }
                    }
                }
            }

            describe("token 재발급") {
                val savedUser =
                    userRepository.save(
                        User(
                            email = "haeni-test-2@gmail.com",
                            provider = SocialProvider.GOOGLE,
                            providerId = "19191919191",
                        ),
                    )

                val refreshToken = jwtTokenProvider.generateRefreshToken(savedUser.id, savedUser.email, savedUser.role)
                refreshTokenRepository.save(
                    RefreshToken(
                        userId = savedUser.id,
                        tokenHash = refreshToken,
                        expiresAt = jwtTokenProvider.getExpiration(refreshToken),
                    ),
                )

                context("만료된 token이 주어지면") {
                    it("토큰을 새로 발급한다.") {
                        val responseDto =
                            authService.refreshToken(
                                RefreshTokenRequestDto(refreshToken = refreshToken),
                            )

                        responseDto.refreshToken shouldNotBe null
                        responseDto.accessToken shouldNotBe null
                    }
                }
            }
        },
    )
