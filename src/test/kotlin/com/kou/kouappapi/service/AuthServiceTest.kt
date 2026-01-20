package com.kou.kouappapi.service

import com.kou.kouappapi.auth.service.AuthService
import com.kou.kouappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.exception.AuthInvalidIdTokenException
import com.kou.kouappapi.manager.couple.CoupleManager
import com.kou.kouappapi.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Tag
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@Tag("local-only")
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AuthServiceTest(
    private val authService: AuthService,
    private val coupleManager: CoupleManager,
    private val userRepository: UserRepository,
) : DescribeSpec(
        {
            describe("google 소셜 로그인") {
                context("유효한 google id 토큰이 주어지면") {
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

            describe("커플 연동 완료") {
                context("유효한 초대 코드가 주어지면") {
                    val inviterUser =
                        userRepository.save(
                            User(
                                email = "haeni-test@gmail.com",
                                provider = SocialProvider.GOOGLE,
                                providerId = "1212313131",
                            ),
                        )
                    val inviteCode = coupleManager.generateInviteCode()
                    coupleManager.saveCoupleInviteCode(inviterUser.id, inviteCode)

                    val responseSocialLogin =
                        authService.socialLogin(
                            SocialLoginRequestDto(
                                provider = SocialProvider.GOOGLE,
                                token = "",
                                inviteCode = inviteCode,
                            ),
                        )

                    it("로그인 토큰을 발행되고, User 테이블에 couple_id가 업데이트 된다.") {
                        responseSocialLogin shouldNotBe null
                        responseSocialLogin.user.coupleId shouldNotBe null
                    }
                }
            }
        },
    )
