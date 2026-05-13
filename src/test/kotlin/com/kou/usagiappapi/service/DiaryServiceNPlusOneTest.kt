package com.kou.usagiappapi.service

import com.kou.usagiappapi.IntegrationTestSupport
import com.kou.usagiappapi.activityCategory.entity.ActivityCategory
import com.kou.usagiappapi.activityCategory.repository.ActivityCategoryRepository
import com.kou.usagiappapi.diary.entity.Diary
import com.kou.usagiappapi.diary.entity.DiaryActivityCategory
import com.kou.usagiappapi.diary.enums.Emotion
import com.kou.usagiappapi.diary.repository.DiaryActivityCategoryRepository
import com.kou.usagiappapi.diary.repository.DiaryRepository
import com.kou.usagiappapi.diary.service.DiaryService
import com.kou.usagiappapi.user.entity.User
import com.kou.usagiappapi.user.enums.SocialProvider
import com.kou.usagiappapi.user.repository.UserRepository
import io.kotest.matchers.longs.shouldBeLessThanOrEqual
import jakarta.persistence.EntityManager
import org.hibernate.SessionFactory
import org.hibernate.stat.Statistics
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Transactional
class DiaryServiceNPlusOneTest(
    private val service: DiaryService,
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository,
    private val activityCategoryRepository: ActivityCategoryRepository,
    private val diaryActivityCategoryRepository: DiaryActivityCategoryRepository,
    private val entityManager: EntityManager,
) : IntegrationTestSupport({

        lateinit var savedUser: User
        lateinit var savedDiary: Diary
        lateinit var statistics: Statistics

        val activityCategoryCount = 5

        beforeEach {
            statistics =
                entityManager.entityManagerFactory
                    .unwrap(SessionFactory::class.java)
                    .statistics
            statistics.isStatisticsEnabled = true

            savedUser =
                userRepository.save(
                    User(
                        name = "n1-user",
                        email = "n1@gmail.com",
                        provider = SocialProvider.GOOGLE,
                        providerId = "n1-test",
                    ),
                )

            val categories =
                activityCategoryRepository.saveAll<ActivityCategory>(
                    (1..activityCategoryCount).map { ActivityCategory(name = "활동$it") },
                )

            savedDiary =
                diaryRepository.save(
                    Diary(
                        user = savedUser,
                        date = LocalDate.parse("2026-04-01"),
                        emotion = Emotion.HAPPY,
                    ),
                )

            val mappings =
                categories.map {
                    DiaryActivityCategory(activityCategory = it, diary = savedDiary)
                }
            savedDiary.diaryActivityCategories.addAll(
                diaryActivityCategoryRepository.saveAll<DiaryActivityCategory>(mappings),
            )

            entityManager.flush()
            entityManager.clear()
            statistics.clear()
        }

        describe("getDiary 쿼리 수 회귀 가드") {
            context("활동 카테고리 ${activityCategoryCount}개를 가진 일기를 단건 조회할 때") {
                // @EntityGraph로 diaryActivityCategories와 그 안의 activityCategory를 fetch graph에 포함시켜
                // 단일 SELECT(JOIN)로 처리한다. 실행 쿼리 수는 활동 카테고리 수와 무관하게 일정해야 한다.
                it("실행 쿼리 수가 활동 카테고리 수와 무관하게 일정하다") {
                    service.getDiary(savedUser.id, savedDiary.id)

                    val queryCount = statistics.prepareStatementCount
                    println("=== getDiary 실행 쿼리 수: $queryCount (활동 카테고리: ${activityCategoryCount}개) ===")

                    queryCount shouldBeLessThanOrEqual 2L
                }
            }
        }
    })
