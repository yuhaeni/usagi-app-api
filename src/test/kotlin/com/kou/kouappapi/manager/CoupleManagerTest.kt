package com.kou.kouappapi.manager

import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.manager.couple.CoupleManager
import com.kou.kouappapi.repository.UserRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CoupleManagerTest(
    private val manager: CoupleManager,
    private val userRepository: UserRepository,
) : DescribeSpec({

        describe("커플 초대 코드 생성") {
            context("올바른 유저 정보가 주어지면") {
                val savedUser =
                    userRepository.save(
                        User(
                            email = "test2@gmail.com",
                            provider = SocialProvider.GOOGLE,
                            providerId = "12345678",
                        ),
                    )

                it("redis에 초대 코드 정보가 저장된다.") {
                    val inviteCode = manager.generateInviteCode()
                    manager.saveCoupleInviteCode(savedUser.id, inviteCode)

                    val inviteCodeValue = manager.getRedisValueForInviteUser(savedUser.id)
                    inviteCodeValue shouldBe inviteCode

                    val inviteUserValue = manager.getRedisValueForInviteCode(inviteCode)
                    inviteUserValue shouldBe savedUser.id.toString()
                }
            }
        }
    })
