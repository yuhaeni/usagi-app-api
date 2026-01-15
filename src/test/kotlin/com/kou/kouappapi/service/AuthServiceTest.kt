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
                                    token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjdiZjU5NTQ4OWEwYmIxNThiMDg1ZTIzZTdiNTJiZjk4OTFlMDQ1MzgiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiI0MDc0MDg3MTgxOTIuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDAxNTc1NjMyNzg1Mzc4Nzc0MTEiLCJlbWFpbCI6ImtvdS5kZXYuY29ycEBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXRfaGFzaCI6InJfV2c5SVFoRHhGMlBFTzBrT1V1cFEiLCJuYW1lIjoi7ZW064uI7ISd7KeE7ZW064uI7ISd7KeEIiwicGljdHVyZSI6Imh0dHBzOi8vbGgzLmdvb2dsZXVzZXJjb250ZW50LmNvbS9hL0FDZzhvY0o0d3hwOURBQTF6TzYxMU5QYUtpUXU2RTVDdWllMS11MVk5QnZydEwtRTNZWi00SG89czk2LWMiLCJnaXZlbl9uYW1lIjoi7ZW064uI7ISd7KeEIiwiZmFtaWx5X25hbWUiOiLtlbTri4jshJ3sp4QiLCJpYXQiOjE3Njg0NDY4NTksImV4cCI6MTc2ODQ1MDQ1OX0.klZiLKh3meVM0FLCKxyQqIhIqvNPA3aOOiW_4iNoV9bc5nNS86GmzfSdlNhaFJpUpF9TAxD89W3papKdYgBiaieUpqHXSfzLgJefBj0ldj1YGYYNAShAh8reHdasOoX_JgTXijHhFpyjC2ZyNWiGU0sfrlDz5MLQcTmIK0hjO9b3ePrfQ5E3-U7GTQllezp88EzQHKP8u4mrdb6I549xJsS1niQZj0Dx8LoeZyxK5q3eL4CUp654X15hPTVWl6DR3OJ8KmyHJ4oOazFXzfhNViFOHGRunB-9UcgmO9KEVt0JsZM46wfEuHrQcv-xYu000CWw8Lztz5w-yWMwxTdnMg",
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
