package com.kou.kouappapi.service

import com.kou.kouappapi.auth.service.AuthService
import com.kou.kouappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.exception.AuthInvalidIdTokenException
import com.kou.kouappapi.manager.couple.CoupleManager
import com.kou.kouappapi.repository.CoupleRepository
import com.kou.kouappapi.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Disabled
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Disabled
@Transactional
class AuthServiceTest(
    private val authService: AuthService,
    private val coupleManager: CoupleManager,
    private val userRepository: UserRepository,
    private val coupleRepository: CoupleRepository,
) : DescribeSpec(
    {
        describe("google 소셜 로그인") {
//            context("유효한 google id 토큰이 주어지면") {
//                it("로그인 토큰을 발행한다.") {
//                    val responseSocialLogin =
//                        authService.socialLogin(
//                            SocialLoginRequestDto(
//                                provider = SocialProvider.GOOGLE,
//                                token = "",
//                            ),
//                        )
//
//                    responseSocialLogin shouldNotBe null
//                }
//            }
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
                            token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjdiZjU5NTQ4OWEwYmIxNThiMDg1ZTIzZTdiNTJiZjk4OTFlMDQ1MzgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDAxNTc1NjMyNzg1Mzc4Nzc0MTEiLCJlbWFpbCI6ImtvdS5kZXYuY29ycEBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6InlVczl5SmZNa1R6TWJydnR4Y1BiQ0EiLCJuYW1lIjoi7ZW064uI7ISd7KeE7ZW064uI7ISd7KeEIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0o0d3hwOURBQTF6TzYxMU5QYUtpUXU2RTVDdWllMS11MVk5QnZydEwtRTNZWi00SG89czk2LWMiLCJnaXZlbl9uYW1lIjoi7ZW064uI7ISd7KeEIiwiZmFtaWx5X25hbWUiOiLtlbTri4jshJ3sp4QiLCJpYXQiOjE3Njg2NTA1NjMsImV4cCI6MTc2ODY1NDE2M30.Yp_7ZU-2sAPMmzjYL5jOqJ0q2GJlukyCiANyEsUIGviH_TtbyAzB9lrPWGwOyBDnm8oEsy-WxexRw_Dqb_BZxsjqvODnkIbMmfJB_gB1O7HohYr10ZtR7VEulm8Dlmt1Pb1ueXPYvqhW3Gi9yHQ7vM_JZtRM4bZxJPmL0Y4AMdPteRWsvmWu8V5Tlgd39PjwPKqGqDTEBpftNEEupobl2LiE7_ZrEo2SuYDXU1S11Ul6ejO6TnY_mPOl9RbM3PlaqNLfkad2iZ2-hokbkzkWMYbR2oMUk7wECo_yYUlIORUkRC0aiNAPrn0ajlhvSBml-1FpT3ZcC8NLRL3Pd6RPWA",
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
