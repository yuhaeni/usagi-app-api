package com.kou.kouappapi.service

import com.kou.kouappapi.IntegrationTestSupport
import com.kou.kouappapi.entity.Diary
import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.Emotion
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.exception.DiaryAlreadyExistsException
import com.kou.kouappapi.repository.DiaryRepository
import com.kou.kouappapi.repository.UserRepository
import com.kou.kouappapi.service.dto.CreateDiaryRequestDto
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class DiaryServiceTest(
    private val service: DiaryService,
    private val userRepository: UserRepository,
    private val diaryRepository: DiaryRepository,
) : IntegrationTestSupport({

        lateinit var savedUser: User
        lateinit var savedDiary: Diary

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
            savedDiary =
                diaryRepository.save(
                    Diary(
                        user = savedUser,
                        date = LocalDate.now(),
                        emotion = Emotion.TIRED,
                    ),
                )
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
                val requestDto = CreateDiaryRequestDto(date = LocalDate.parse("2026-02-18"), emotion = Emotion.NEUTRAL)
                it("날짜, 감정이 저장된다") {
                    val response = service.createDiary(savedUser.id, requestDto)
                    response.date shouldBe LocalDate.parse("2026-02-18")
                    response.emotion shouldBe Emotion.NEUTRAL
                    response.content shouldBe null
                }
            }

            context("날짜, 감정, 내용을 입력한 경우") {
                val requestDto =
                    CreateDiaryRequestDto(
                        date = LocalDate.parse("2026-02-10"),
                        emotion = Emotion.HOPELESS,
                        content = "에잇 몰라!",
                    )
                it("날짜, 감정, 내용이 저장된다") {
                    val response = service.createDiary(savedUser.id, requestDto)
                    response.date shouldBe LocalDate.parse("2026-02-10")
                    response.emotion shouldBe Emotion.HOPELESS
                    response.content shouldBe "에잇 몰라!"
                }
            }
        }
    })
