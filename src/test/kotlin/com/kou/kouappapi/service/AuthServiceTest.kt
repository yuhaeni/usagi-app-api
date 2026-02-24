package com.kou.kouappapi.service

import com.kou.kouappapi.IntegrationTestSupport
import com.kou.kouappapi.auth.exception.AuthInvalidIdTokenException
import com.kou.kouappapi.auth.service.AuthService
import com.kou.kouappapi.auth.service.dto.RefreshTokenRequestDto
import com.kou.kouappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.kouappapi.entity.RefreshToken
import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.repository.RefreshTokenRepository
import com.kou.kouappapi.repository.UserRepository
import com.kou.kouappapi.security.jwt.JwtTokenProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.annotation.Tags
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.util.Date

@Tags("local-only")
@SpringBootTest
@ActiveProfiles("test")
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
