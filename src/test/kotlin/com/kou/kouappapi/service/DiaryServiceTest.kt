package com.kou.kouappapi.service

import com.kou.kouappapi.IntegrationTestSupport
import com.kou.kouappapi.entity.ActivityCategory
import com.kou.kouappapi.entity.Diary
import com.kou.kouappapi.entity.DiaryActivityCategory
import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.Emotion
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.exception.DiaryAlreadyExistsException
import com.kou.kouappapi.exception.DiaryNotFoundException
import com.kou.kouappapi.exception.NotDiaryOwnerException
import com.kou.kouappapi.repository.ActivityCategoryRepository
import com.kou.kouappapi.repository.DiaryActivityCategoryRepository
import com.kou.kouappapi.repository.DiaryRepository
import com.kou.kouappapi.repository.UserRepository
import com.kou.kouappapi.service.dto.CreateDiaryRequestDto
import com.kou.kouappapi.service.dto.UpdateDiaryRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Transactional
class DiaryServiceTest(
    private val service: DiaryService,
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository,
    private val activityCategoryRepository: ActivityCategoryRepository,
    private val diaryActivityCategoryRepository: DiaryActivityCategoryRepository,
) : IntegrationTestSupport({

        lateinit var savedUser: User
        lateinit var otherUser: User
        lateinit var savedDiary: Diary
        lateinit var savedDiary2: Diary
        lateinit var savedDiary3: Diary
        lateinit var savedDiary4: Diary
        lateinit var savedDiary5: Diary
        lateinit var savedDiary6: Diary
        lateinit var savedDiary7: Diary
        lateinit var defaultActivityCategoryList: List<ActivityCategory>

        beforeEach {

            defaultActivityCategoryList =
                activityCategoryRepository.saveAll<ActivityCategory>(
                    listOf(
                        ActivityCategory(name = "데이트"),
                        ActivityCategory(name = "친구 모임"),
                        ActivityCategory(name = "가족 행사"),
                        ActivityCategory(name = "업무"),
                        ActivityCategory(name = "혼자만의 시간"),
                    ),
                )

            savedUser =
                userRepository.save(
                    User(
                        name = "test",
                        email = "test@gmail.com",
                        provider = SocialProvider.GOOGLE,
                        providerId = "123456789877",
                    ),
                )
            otherUser =
                userRepository.save(
                    User(
                        name = "test2",
                        email = "test2@gmail.com",
                        provider = SocialProvider.GOOGLE,
                        providerId = "987654321",
                    ),
                )
            savedDiary =
                diaryRepository.save(
                    Diary(
                        user = savedUser,
                        date = LocalDate.now(),
                        emotion = Emotion.NEUTRAL,
                    ),
                )

            savedDiary2 =
                diaryRepository.save(
                    Diary(
                        user = savedUser,
                        date = LocalDate.parse("2026-02-01"),
                        emotion = Emotion.NEUTRAL,
                    ),
                )

            savedDiary3 =
                diaryRepository.save(
                    Diary(
                        user = savedUser,
                        date = LocalDate.parse("2026-02-22"),
                        emotion = Emotion.HOPELESS,
                    ),
                )

            savedDiary4 =
                diaryRepository.save(
                    Diary(
                        user = savedUser,
                        date = LocalDate.parse("2026-01-02"),
                        emotion = Emotion.HOPELESS,
                    ),
                )

            savedDiary5 =
                diaryRepository.save(
                    Diary(
                        user = savedUser,
                        date = LocalDate.parse("2026-01-03"),
                        emotion = Emotion.HOPELESS,
                    ),
                )

            savedDiary6 =
                diaryRepository.save(
                    Diary(
                        user = savedUser,
                        date = LocalDate.parse("2026-01-08"),
                        emotion = Emotion.HOPELESS,
                    ),
                )

            savedDiary7 =
                diaryRepository.save(
                    Diary(
                        user = savedUser,
                        date = LocalDate.parse("2026-01-30"),
                        emotion = Emotion.HOPELESS,
                    ),
                )

            val activityCategories = listOf(defaultActivityCategoryList[0], defaultActivityCategoryList[1])
            val diaryActivityCategories =
                activityCategories.map {
                    DiaryActivityCategory(activityCategory = it, diary = savedDiary7)
                }
            val savedDiaryActivityCategories =
                diaryActivityCategoryRepository.saveAll<DiaryActivityCategory>(
                    diaryActivityCategories,
                )
            savedDiary7.diaryActivityCategories.addAll(savedDiaryActivityCategories)
        }

        describe("일기(감정) 작성") {

            context("해당 날짜에 이미 일기가 작성된 경우") {
                it("DiaryAlreadyExistsException이 발생한다.") {
                    val requestDto = CreateDiaryRequestDto(date = LocalDate.now(), emotion = Emotion.TIRED)
                    shouldThrow<DiaryAlreadyExistsException> {
                        service.createDiary(savedUser.id, requestDto)
                    }
                }
            }

            context("날짜와 감정만 입력한 경우") {
                it("날짜, 감정이 저장된다") {
                    val requestDto =
                        CreateDiaryRequestDto(date = LocalDate.parse("2026-02-18"), emotion = Emotion.NEUTRAL)
                    val response = service.createDiary(savedUser.id, requestDto)
                    response.date shouldBe LocalDate.parse("2026-02-18")
                    response.emotion shouldBe Emotion.NEUTRAL
                    response.content shouldBe null
                }
            }

            context("날짜, 감정, 내용을 입력한 경우") {
                it("날짜, 감정, 내용이 저장된다") {
                    val requestDto =
                        CreateDiaryRequestDto(
                            date = LocalDate.parse("2026-02-10"),
                            emotion = Emotion.HOPELESS,
                            content = "에잇 몰라!",
                        )
                    val response = service.createDiary(savedUser.id, requestDto)
                    response.date shouldBe LocalDate.parse("2026-02-10")
                    response.emotion shouldBe Emotion.HOPELESS
                    response.content shouldBe "에잇 몰라!"
                }
            }

            context("날짜, 감정, 활동 카테고리를 입력한 경우") {
                it("날짜, 감정, 활동이 저장된다") {
                    val requestDto =
                        CreateDiaryRequestDto(
                            date = LocalDate.parse("2026-03-05"),
                            emotion = Emotion.NEUTRAL,
                            content = "이너피스",
                            activityCategoryIds =
                                listOf(
                                    defaultActivityCategoryList.get(3).id,
                                    defaultActivityCategoryList.get(4).id,
                                ),
                        )
                    val response = service.createDiary(savedUser.id, requestDto)
                    response.date shouldBe requestDto.date
                    response.emotion shouldBe requestDto.emotion
                    response.content shouldBe requestDto.content
                    response.diaryActivityCategories.size shouldBe 2
                }
            }
        }

        describe("일기(감정) 상세 조회") {

            context("올바른 userId,diaryId를 입력한 경우") {
                it("저장된 일기와 일치하는 데이터가 반환된다.") {
                    val response = service.getDiary(savedUser.id, savedDiary.id)

                    response.date shouldBe savedDiary.date
                    response.emotion shouldBe savedDiary.emotion
                    response.content shouldBe savedDiary.content
                }
            }

            context("다른 사용자의 조회하려는 경우") {
                it("NotDiaryOwnerException이 발생한다") {
                    shouldThrow<NotDiaryOwnerException> {
                        service.getDiary(otherUser.id, savedDiary.id)
                    }
                }
            }
        }

        describe("일기(감정) 수정") {

            context("감정을 입력한 경우") {
                it("감정만 변경된다.") {
                    val request = UpdateDiaryRequestDto(emotion = Emotion.HOPELESS)
                    val response = service.updateDiary(savedUser.id, savedDiary.id, request)

                    response.date shouldBe savedDiary.date
                    response.emotion shouldBe request.emotion
                }
            }

            context("감정,내용을 입력한 경우") {
                it("감정,내용이 변경된다.") {
                    val request = UpdateDiaryRequestDto(emotion = Emotion.HAPPY)
                    val response = service.updateDiary(savedUser.id, savedDiary.id, request)

                    response.date shouldBe savedDiary.date
                    response.emotion shouldBe request.emotion
                    response.content shouldBe request.content
                }
            }

            context("다른 사용자의 수정하려는 경우") {
                it("NotDiaryOwnerException이 발생한다") {
                    val request = UpdateDiaryRequestDto(emotion = Emotion.NEUTRAL)
                    shouldThrow<NotDiaryOwnerException> {
                        service.updateDiary(otherUser.id, savedDiary.id, request)
                    }
                }
            }

            context("활동 카테고리가 없던 일기에 새로운 활동 카테고리를 추가하는 경우") {
                it("활동 카테고리 내용이 추가된다.") {
                    val request =
                        UpdateDiaryRequestDto(
                            activityCategoryIdList =
                                listOf(
                                    defaultActivityCategoryList.get(3).id,
                                ),
                        )
                    val response = service.updateDiary(savedUser.id, savedDiary6.id, request)
                    response.diaryActivityCategories[0].id shouldBe defaultActivityCategoryList.get(3).id
                }
            }
            context("이미 있던 활동 카테고리를 변경하는 경우") {
                it("활동 카테고리 내용이 변경된다.") {
                    val request =
                        UpdateDiaryRequestDto(
                            activityCategoryIdList =
                                listOf(
                                    defaultActivityCategoryList[4].id,
                                ),
                        )
                    val response = service.updateDiary(savedUser.id, savedDiary7.id, request)
                    response.diaryActivityCategories[0].id shouldBe defaultActivityCategoryList[4].id
                }
            }

            context("이미 있던 활동 카테고리를 제거하는 경우") {
                it("활동 카테고리 내용이 삭제된다.") {
                    val request =
                        UpdateDiaryRequestDto(
                            activityCategoryIdList = emptyList(),
                        )
                    val response = service.updateDiary(savedUser.id, savedDiary7.id, request)
                    response.diaryActivityCategories shouldBe emptyList()
                }
            }
        }

        describe("일기(감정) 삭제") {

            context("다른 사용자의 일기를 삭제하려는 경우") {
                it("NotDiaryOwnerException이 발생한다") {
                    shouldThrow<NotDiaryOwnerException> {
                        service.deleteDiary(otherUser.id, savedDiary.id)
                    }
                }
            }

            context("존재하지 않는 일기를 삭제하는 경우") {
                it("DiaryNotFoundException이 발생한다.") {
                    shouldThrow<DiaryNotFoundException> {
                        service.deleteDiary(savedUser.id, 99999999L)
                    }
                }
            }

            context("존재하는 일기를 삭제하는 경우") {
                it("일기가 DB에서 삭제된다") {
                    service.deleteDiary(savedUser.id, savedDiary.id)

                    diaryRepository.findByIdOrNull(savedDiary.id) shouldBe null
                }
            }
        }

        describe("일기 목록 조회") {
            context("요청 날짜가 없는 경우") {
                it("현재 월 기준으로 일기 목록이 조회된다.") {
                    val response = service.getDiaryList(savedUser.id)
                    response.size shouldNotBe 0
                }
            }
            context("요청 날짜가 있는 경우") {
                it("해당 날짜 기준으로 일기 목록이 조회된다.") {
                    val response =
                        service.getDiaryList(
                            savedUser.id,
                            LocalDate.parse("2026-01-01"),
                            LocalDate.parse("2026-01-31"),
                        )
                    response.size shouldBe 4
                }
            }
        }
    })
