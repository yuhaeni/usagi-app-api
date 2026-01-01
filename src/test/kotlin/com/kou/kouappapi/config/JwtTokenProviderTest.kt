package com.kou.kouappapi.config

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import security.jwt.JwtProperties
import security.jwt.JwtTokenProvider

class JwtTokenProviderTest : DescribeSpec({

    val jwtProperties = JwtProperties(
        secret = "q9T2eL0mR7P6K1d4vZxW3S5b8HnYJ0aQfC+UOeM9Lw=",
        accessTokenExpireTime = 1800000,
        refreshTokenExpireTime = 604800000
    )

    val jwtTokenProvider = JwtTokenProvider(jwtProperties)

    describe("access token 생성") {
        describe("user 정보가 주어지면") {
            it("access token을 생성한다.") {
                val userId = 1L
                val email = "test@example.com"
                val token = jwtTokenProvider.generateAccessToken(userId, email)

                token shouldNotBe null
                token.split(".").size shouldBe 3
            }
        }
    }

    describe("access token 검증") {

        context("유효한 토큰인 경우") {
            it("true를 반환한다.") {
                val userId = 1L
                val email = "test@example.com"
                val token = jwtTokenProvider.generateAccessToken(userId, email)

                val isValid = jwtTokenProvider.validateToken(token)

                isValid shouldBe true
            }
        }

        context("잘못된 형식의 토큰인 경우") {
            it("false를 반환한다.") {
                // given
                val invalidToken = "invalid.token.format"

                // when
                val isValid = jwtTokenProvider.validateToken(invalidToken)

                // then
                isValid shouldBe false
            }
        }
    }

    describe("token 추출") {
        it("토큰에서 email을 추출한다") {
            val userId = 1L
            val email = "test@example.com"
            val token = jwtTokenProvider.generateAccessToken(userId, email)

            val extractedEmail = jwtTokenProvider.getEmail(token)

            extractedEmail shouldBe email
        }
    }

})
