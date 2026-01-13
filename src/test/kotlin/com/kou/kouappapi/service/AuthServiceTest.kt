package com.kou.kouappapi.service

import com.kou.kouappapi.auth.service.AuthService
import com.kou.kouappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.exception.AuthInvalidIdTokenException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Disabled
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Disabled
@Transactional
class AuthServiceTest(
    private val authService: AuthService,
) : DescribeSpec(
        {
            describe("google 소셜 로그인") {

                describe("유효한 google id 토큰이 주어지면") {
                    it("로그인 토큰을 발행한다.") {
                        val responseSocialLogin =
                            authService.socialLogin(
                                SocialLoginRequestDto(
                                    provider = SocialProvider.GOOGLE,
                                    token = "",
                                ),
                            )

                        responseSocialLogin shouldNotBe null
                    }
                }
                describe("유효하지 않은 google id 토큰이 주어지면") {
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
        },
    )
