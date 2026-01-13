package com.kou.kouappapi.service

import com.kou.kouappapi.auth.service.dto.CompleteProfileRequestDto
import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.manager.image.ImageNotFoundException
import com.kou.kouappapi.repository.UserRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Disabled
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@Disabled
class UserServiceTest(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) : DescribeSpec(
        {
            describe("회원 가입 후 프로필 설정") {

                context("유효한 유저 ID와 요청값으로 이름이 주어지면") {
                    it("해당 유저의 이름의 이름이 요청값으로 변경된다.") {
                        val savedUser =
                            userRepository.save(
                                User(
                                    email = "test2@gmail.com",
                                    provider = SocialProvider.GOOGLE,
                                    providerId = "12345678",
                                ),
                            )

                        val responseDto =
                            userService.completeProfile(
                                CompleteProfileRequestDto(
                                    userId = savedUser.id,
                                    name = "Test User2",
                                ),
                            )

                        val user = userRepository.findByIdOrNull(responseDto.userId)
                        user?.name shouldBe "Test User2"
                    }
                }

                context("유효한 유저 ID와 요청값으로 비밀번호가 주어지면") {
                    it("해당 유저의 비밀번호가 요청값으로 변경된다.") {
                        val savedUser =
                            userRepository.save(
                                User(
                                    email = "test3@gmail.com",
                                    provider = SocialProvider.GOOGLE,
                                    providerId = "123456789",
                                ),
                            )

                        val responseDto =
                            userService.completeProfile(
                                CompleteProfileRequestDto(
                                    userId = savedUser.id,
                                    name = "Test User3",
                                    password = "12345678@@",
                                ),
                            )

                        val user = userRepository.findByIdOrNull(responseDto.userId)
                        passwordEncoder.matches("12345678@@", user?.password) shouldBe true
                    }
                }

                context("유효한 유저 ID와 요청값으로 프로필 이미지가 주어지면") {
                    it("해당 유저의 프로필 url이 포함된 응답값이 반환된다.") {
                        val savedUser =
                            userRepository.save(
                                User(
                                    email = "test4@gmail.com",
                                    provider = SocialProvider.GOOGLE,
                                    providerId = "12345678911",
                                ),
                            )

                        val inputStream =
                            javaClass.classLoader
                                .getResourceAsStream("images/꽃을든_치이카와.jpeg")
                                ?: throw ImageNotFoundException()

                        val file =
                            MockMultipartFile(
                                "image",
                                "integration-test.jpeg",
                                "image/jpeg",
                                inputStream,
                            )

                        val responseDto =
                            userService.completeProfile(
                                CompleteProfileRequestDto(
                                    userId = savedUser.id,
                                    name = "Test User3",
                                    profileFile = file,
                                ),
                            )

                        val user = userRepository.findByIdOrNull(responseDto.userId)
                        user?.profileImageId shouldNotBe null
                        user?.profileCompleted shouldBe true
                    }
                }

                context("이미 프로필 설정을 완료한 경우") {
                    it("이미 프로필 설정 완료한 사용자라는 예외를 발생시킨다.") {
                    }
                }
            }
        },
    )
