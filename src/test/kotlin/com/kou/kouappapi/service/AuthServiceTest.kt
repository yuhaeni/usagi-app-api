package com.kou.kouappapi.service

import com.kou.kouappapi.IntegrationTestSupport
import com.kou.kouappapi.auth.service.AuthService
import com.kou.kouappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.exception.AuthInvalidIdTokenException
import com.kou.kouappapi.manager.couple.CoupleManager
import com.kou.kouappapi.repository.RefreshTokenRepository
import com.kou.kouappapi.repository.UserRepository
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
    private val coupleManager: CoupleManager,
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
) : IntegrationTestSupport(
        {
            describe("google 소셜 로그인") {
                context("유효한 google id 토큰이 주어지면") {
                    it("로그인 토큰을 발행하고 refresh token을 DB에 저장한다") {
                        val responseSocialLogin =
                            authService.socialLogin(
                                SocialLoginRequestDto(
                                    provider = SocialProvider.GOOGLE,
                                    token = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg2MzBhNzFiZDZlYzFjNjEyNTdhMjdmZjJlZmQ5MTg3MmVjYWIxZjYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDAxNTc1NjMyNzg1Mzc4Nzc0MTEiLCJlbWFpbCI6ImtvdS5kZXYuY29ycEBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6Ik5JcXZBMGl1aUxvOGFlNllqSkUwMEEiLCJuYW1lIjoi7ZW064uI7ISd7KeE7ZW064uI7ISd7KeEIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0o0d3hwOURBQTF6TzYxMU5QYUtpUXU2RTVDdWllMS11MVk5QnZydEwtRTNZWi00SG89czk2LWMiLCJnaXZlbl9uYW1lIjoi7ZW064uI7ISd7KeEIiwiZmFtaWx5X25hbWUiOiLtlbTri4jshJ3sp4QiLCJpYXQiOjE3NzAwOTgxNDgsImV4cCI6MTc3MDEwMTc0OH0.L4TaER-42sDarge2CQatGqz0rOD7XDOAf7m8sdvm8wphswcPWTIFwO5TmjGbOUfmwk4bC9_gApniMo7WbfaHszo_bApGbBZi3Spz3Wb3uS86WFpAZB2BhJB5DYybuplRx4pfnEBwluZohPL9-WEcwIDQiGm9Z855rRrn7mCr0CbzG8euYZeZxvonmfmiW7YXwmu69e_z3hASbKEkwBDi4oNP9KiJQ_VyBlVsRC8qXmrBM77NzZDNHnIlnaeFiXM_QjgArHI-ZYetyvOwYH5zX2WgCpWEqJJnQmw007FawZD4enZXQYU0RdELmkgUx9j-DMZzZOpsz4MaXty6_Lo71g",
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
                                token = "eyJhbGciOiJSUzI1NiIsImtpZCI6Ijg2MzBhNzFiZDZlYzFjNjEyNTdhMjdmZjJlZmQ5MTg3MmVjYWIxZjYiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDAxNTc1NjMyNzg1Mzc4Nzc0MTEiLCJlbWFpbCI6ImtvdS5kZXYuY29ycEBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6Ik5JcXZBMGl1aUxvOGFlNllqSkUwMEEiLCJuYW1lIjoi7ZW064uI7ISd7KeE7ZW064uI7ISd7KeEIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0o0d3hwOURBQTF6TzYxMU5QYUtpUXU2RTVDdWllMS11MVk5QnZydEwtRTNZWi00SG89czk2LWMiLCJnaXZlbl9uYW1lIjoi7ZW064uI7ISd7KeEIiwiZmFtaWx5X25hbWUiOiLtlbTri4jshJ3sp4QiLCJpYXQiOjE3NzAwOTgxNDgsImV4cCI6MTc3MDEwMTc0OH0.L4TaER-42sDarge2CQatGqz0rOD7XDOAf7m8sdvm8wphswcPWTIFwO5TmjGbOUfmwk4bC9_gApniMo7WbfaHszo_bApGbBZi3Spz3Wb3uS86WFpAZB2BhJB5DYybuplRx4pfnEBwluZohPL9-WEcwIDQiGm9Z855rRrn7mCr0CbzG8euYZeZxvonmfmiW7YXwmu69e_z3hASbKEkwBDi4oNP9KiJQ_VyBlVsRC8qXmrBM77NzZDNHnIlnaeFiXM_QjgArHI-ZYetyvOwYH5zX2WgCpWEqJJnQmw007FawZD4enZXQYU0RdELmkgUx9j-DMZzZOpsz4MaXty6_Lo71g",
                                inviteCode = inviteCode,
                            ),
                        )

                    it("로그인 토큰을 발행된다.") {
                        responseSocialLogin shouldNotBe null
                    }
                }
            }
        },
    )
