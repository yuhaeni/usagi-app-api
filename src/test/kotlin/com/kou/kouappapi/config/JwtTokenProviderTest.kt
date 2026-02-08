package com.kou.kouappapi.config

import com.kou.kouappapi.enums.Role
import com.kou.kouappapi.security.jwt.JwtProperties
import com.kou.kouappapi.security.jwt.JwtTokenProvider
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class JwtTokenProviderTest :
    DescribeSpec({

        val jwtProperties =
            JwtProperties(
                secret = "q9T2eL0mR7P6K1d4vZxW3S5b8HnYJ0aQfC+UOeM9Lw=",
                accessTokenExpireDuration = 1800000,
                refreshTokenExpireDuration = 604800000,
            )

        val jwtTokenProvider = JwtTokenProvider(jwtProperties)

        describe("access token 생성") {
            describe("user 정보가 주어지면") {
                it("access token을 생성한다.") {
                    val userId = 1L
                    val email = "test@example.com"
                    val role = Role.USER
                    val token = jwtTokenProvider.generateAccessToken(userId, email, role)

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
                    val role = Role.USER
                    val token = jwtTokenProvider.generateAccessToken(userId, email, role)

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
                val role = Role.USER
                val token = jwtTokenProvider.generateAccessToken(userId, email, role)

                val extractedEmail = jwtTokenProvider.getEmailFromToken(token)

                extractedEmail shouldBe email
            }
            it("토큰에서 role을 추출한다") {
                val userId = 1L
                val email = "test@example.com"
                val role = Role.USER
                val token = jwtTokenProvider.generateAccessToken(userId, email, role)

                val extractedRole = jwtTokenProvider.getRoleFromToken(token)

                extractedRole shouldBe extractedRole
            }
        }

        fun createToken(days: Long) =
            jwtTokenProvider.generateToken(
                userId = 1L,
                email = "test@example.com",
                role = Role.USER,
                expireDuration = days * 86400000,
                tokenType = "REFRESH",
            )

        describe("만료 예정 리프레시 토큰 확인") {

            it("7일 이내 만료 예정이면 true") {
                jwtTokenProvider.shouldRefreshToken(createToken(7)) shouldBe true
                jwtTokenProvider.shouldRefreshToken(createToken(3)) shouldBe true
                jwtTokenProvider.shouldRefreshToken(createToken(1)) shouldBe true
            }

            it("8일 이상 남았으면 false") {
                jwtTokenProvider.shouldRefreshToken(createToken(8)) shouldBe false
                jwtTokenProvider.shouldRefreshToken(createToken(30)) shouldBe false
            }
        }
    })
