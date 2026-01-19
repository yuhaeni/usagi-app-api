package com.kou.kouappapi.manager

import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.manager.couple.CoupleManager
import com.kou.kouappapi.repository.CoupleRepository
import com.kou.kouappapi.repository.UserRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CoupleManagerTest(
    private val manager: CoupleManager,
    private val userRepository: UserRepository,
    private val coupleRepository: CoupleRepository,
) : DescribeSpec({

        describe("커플 초대 코드 생성") {
            context("올바른 유저 정보가 주어지면") {
                val savedUser =
                    userRepository.save(
                        User(
                            email = "test@gmail.com",
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

        describe("커플 연동") {
            context("올바른 초대 코드와 초대 받은 유저 정보가 들어오면") {
                val savedUser =
                    userRepository.save(
                        User(
                            email = "test1@gmail.com",
                            provider = SocialProvider.GOOGLE,
                            providerId = "111111",
                        ),
                    )
                val inviteCode = manager.generateInviteCode()
                manager.saveCoupleInviteCode(savedUser.id, inviteCode)

                val savedUser2 =
                    userRepository.save(
                        User(
                            email = "test2@gmail.com",
                            provider = SocialProvider.GOOGLE,
                            providerId = "222222",
                        ),
                    )

                it("커플 테이블에 행이 추가되고, 유저의 couple_id 값이 추가된다.") {
                    val coupleId = manager.completeCoupleConnection(savedUser2.id, inviteCode)

                    val couple = coupleRepository.findByIdOrNull(coupleId)
                    couple shouldNotBe null
                    userRepository.findByIdOrNull(savedUser.id)?.coupleId shouldBe couple?.id
                    userRepository.findByIdOrNull(savedUser2.id)?.coupleId shouldBe couple?.id
                }
            }
        }
    })
