package com.kou.kouappapi.service

import com.kou.kouappapi.IntegrationTestSupport
import com.kou.kouappapi.entity.ActivityCategory
import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.repository.ActivityCategoryRepository
import com.kou.kouappapi.repository.UserRepository
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ActivityCategoryServiceTest(
    val userRepository: UserRepository,
    val activityCategoryRepository: ActivityCategoryRepository,
    val activityCategoryService: ActivityCategoryService,
) : IntegrationTestSupport({

        lateinit var savedUser: User
        lateinit var defaultActivityCategoryList: List<ActivityCategory>

        beforeEach {
            savedUser =
                userRepository.save(
                    User(
                        name = "test",
                        email = "test@gmail.com",
                        provider = SocialProvider.GOOGLE,
                        providerId = "123456789877",
                    ),
                )

            defaultActivityCategoryList =
                listOf(
                    ActivityCategory(name = "데이트"),
                    ActivityCategory(name = "친구 모임"),
                    ActivityCategory(name = "가족 행사"),
                    ActivityCategory(name = "업무"),
                    ActivityCategory(name = "혼자만의 시간"),
                )
            activityCategoryRepository.saveAll<ActivityCategory>(defaultActivityCategoryList)
        }

        describe("활동 카테고리 목록 조회") {
            context("올바른 유저 정보가 들어오면") {
                it("기본 카테고리가 조회된다.)") {
                    val response = activityCategoryService.getActivityCategoryList(savedUser.id)
                    response.size shouldBe 5
                }
            }
        }
    })
