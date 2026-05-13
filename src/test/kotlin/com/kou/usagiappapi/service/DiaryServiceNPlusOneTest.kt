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
import com.kou.usagiappapi.diary.service.dto.UpdateDiaryRequestDto
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

        describe("updateDiary 쿼리 수 회귀 가드") {
            context("기존 N개의 매핑을 새 목록으로 교체할 때") {
                // 기존 구현은 컬렉션 clear() + orphanRemoval로 N개 개별 DELETE 발생 (총 N+3).
                // bulk delete + detach 패턴으로 단일 DELETE로 합쳐 항상 4쿼리(find + findAllById + bulk DELETE + INSERT)에 수렴.
                it("발생 쿼리 수가 기존 매핑 수와 무관하게 일정하다") {
                    val newCategoryIds =
                        activityCategoryRepository
                            .saveAll<ActivityCategory>(listOf(ActivityCategory(name = "새활동")))
                            .map { it.id }
                    entityManager.flush()
                    entityManager.clear()
                    statistics.clear()

                    service.updateDiary(
                        savedUser.id,
                        savedDiary.id,
                        UpdateDiaryRequestDto(activityCategoryIds = newCategoryIds),
                    )

                    val queryCount = statistics.prepareStatementCount
                    println("=== updateDiary 실행 쿼리 수: $queryCount ===")

                    queryCount shouldBeLessThanOrEqual 5L
                }
            }
        }

        describe("getDiaries 쿼리 수 회귀 가드") {
            context("같은 월에 여러 일기가 존재할 때") {
                // 현재 GetDiariesResponseDto는 id/date/emotion만 노출하므로 LAZY 컬렉션을 건드리지 않고
                // 단일 SELECT로 끝난다. 응답 DTO에 활동 카테고리 등을 추가하면 N+1로 발전할 위험이 있어
                // 회귀 가드로 일기 수와 무관하게 1쿼리만 발생하는지 검증한다.
                it("실행 쿼리 수가 일기 수와 무관하게 1번이다") {
                    diaryRepository.saveAll<Diary>(
                        (2..5).map { day ->
                            Diary(
                                user = savedUser,
                                date = LocalDate.parse("2026-04-0$day"),
                                emotion = Emotion.NEUTRAL,
                            )
                        },
                    )
                    entityManager.flush()
                    entityManager.clear()
                    statistics.clear()

                    val diaries =
                        service.getDiaries(
                            savedUser.id,
                            LocalDate.parse("2026-04-01"),
                            LocalDate.parse("2026-04-30"),
                        )

                    val queryCount = statistics.prepareStatementCount
                    println("=== getDiaries 실행 쿼리 수: $queryCount (일기 수: ${diaries.size}개) ===")

                    queryCount shouldBeLessThanOrEqual 1L
                }
            }
        }
    })
