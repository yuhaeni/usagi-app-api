package com.kou.kouappapi.service

import com.kou.kouappapi.IntegrationTestSupport
import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.repository.UserRepository
import io.kotest.core.annotation.Tags
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@Tags("local-only")
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserServiceTest(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : IntegrationTestSupport(
        {
            describe("유저 정보 조회") {
                context("올바른 유저 정보가 주어지면") {
                    val savedUser =
                        userRepository.save(
                            User(
                                name = "해니 테스트",
                                email = "test@gmail.com",
                                provider = SocialProvider.GOOGLE,
                                providerId = "12345678",
                            ),
                        )
                    it("유저 프로필 정보가 조회된다.") {
                        val responseDto = userService.getUserProfile(savedUser.id)

                        responseDto.userId shouldBe savedUser.id
                        responseDto.email shouldBe savedUser.email
                        responseDto.name shouldBe savedUser.name
                    }
                }
            }
        },
    )
