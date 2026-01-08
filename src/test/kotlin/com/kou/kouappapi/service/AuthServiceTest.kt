package com.kou.kouappapi.service

import com.kou.kouappapi.auth.service.AuthService
import com.kou.kouappapi.auth.service.dto.SocialLoginRequestDto
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.exception.AuthInvalidIdTokenException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest(
    private val authService: AuthService,
) : DescribeSpec(
    {
        describe("google 소셜 로그인") {

            describe("유효한 google id 토큰이 주어지면") {
                it("로그인 토큰을 발행한다.") {
                    val responseSocialLogin = authService.socialLogin(
                        SocialLoginRequestDto(
                            provider = SocialProvider.GOOGLE,
                            token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjRiYTZlZmVmNWUxNzIxNDk5NzFhMmQzYWJiNWYzMzJlMGY3ODcxNjUiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDI3NDM5NTA2NDE4MzA4MTY3MjAiLCJlbWFpbCI6ImhhZW5pMzExQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoiNlRoNG43NnpvUzZyME42MlNmLTktUSIsIm5hbWUiOiJobiB5IiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0xNRTVDWWowbXYzM3ZEQXNzaEVyM1JqMzJPMFZfSXZWaHkyWnhUalBKQy1RNGhxaTFqPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6ImhuIiwiZmFtaWx5X25hbWUiOiJ5IiwiaWF0IjoxNzY3ODUwOTk3LCJleHAiOjE3Njc4NTQ1OTd9.iJAdWCuNW-_Z3JNFTHO35PV4J7g7LTGQkKpTN5z3TnLixkGMPCJaVkbxcaJVQ7z_9n_TAZNUxg2Ah3R1LZuLiDBvRTG4cKYb4WUYp7FwWQ0FUAm_Kxe51JMTik5s9_LjE2QH7y-nNPu30ndijK-UuxZfObrFzCsy-AxNAbRqMNT5X26bAiX8zd_9UikCpIOPR08bdcFf0QR8PoC4myQrHc5EHJrXdvLM5pg7blCO0qtlnTaiAtmTw3LypKfkmA9rkuXNZTxkcuyG08Pf5hPFWXs_HyVo1sQ8BnQ_AxR_K1gxEz_ZHxX6UvQHjjaET-DlRPNN_GqBNrcRUikOyzYGrQ"
                        )
                    )

                    responseSocialLogin shouldNotBe null
                }
            }
            describe("유효하지 않은 google id 토큰이 주어지면") {
                it("유효하지 않은 토큰 예외가 발생한다."){
                    shouldThrow<AuthInvalidIdTokenException> {
                        authService.socialLogin(
                            SocialLoginRequestDto(
                                provider = SocialProvider.GOOGLE,
                                token = "test.a0Aa7pCA8ZifEakG0LN3lPCO-fMfVoJdsfaafsdsdf7i3U9Mx8hvqK0tSfO821HLsdArB9Twdu8xV4fP-sguhQk2kQ1t6od06OXu5UAO9mFpGgiamL8gPaQgf0gaCdafsdfadfsdf6"
                            )
                        )
                    }
                }
            }
        }
    }
)
