package com.kou.usagiappapi.diary.service

import com.kou.usagiappapi.diary.service.dto.CreateDiaryRequestDto
import com.kou.usagiappapi.diary.service.dto.CreateDiaryResponseDto
import com.kou.usagiappapi.diary.service.dto.GetDiaryResponseDto
import com.kou.usagiappapi.diary.service.dto.UpdateDiaryRequestDto
import com.kou.usagiappapi.diary.service.dto.UpdateDiaryResponseDto
import com.kou.usagiappapi.entity.Diary
import com.kou.usagiappapi.entity.DiaryActivityCategory
import com.kou.usagiappapi.entity.toResponseDto
import com.kou.usagiappapi.exception.ActivityCategoryNotFoundException
import com.kou.usagiappapi.exception.DiaryAlreadyExistsException
import com.kou.usagiappapi.exception.DiaryNotFoundException
import com.kou.usagiappapi.exception.NotDiaryOwnerException
import com.kou.usagiappapi.exception.UserNotFoundException
import com.kou.usagiappapi.manager.image.ImageManager
import com.kou.usagiappapi.manager.image.ImageUploadResult
import com.kou.usagiappapi.property.CloudinaryProperties
import com.kou.usagiappapi.repository.ActivityCategoryRepository
import com.kou.usagiappapi.repository.DiaryActivityCategoryRepository
import com.kou.usagiappapi.repository.DiaryRepository
import com.kou.usagiappapi.repository.UserRepository
import com.kou.usagiappapi.service.GetDiaryListResponseDto
import com.kou.usagiappapi.service.dto.ActivityCategoryResponseDto
import com.kou.usagiappapi.tool.DateTool
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class DiaryService(
    val diaryRepository: DiaryRepository,
    val userRepository: UserRepository,
    val activityCategoryRepository: ActivityCategoryRepository,
    val diaryActivityCategoryRepository: DiaryActivityCategoryRepository,
    val imageManager: ImageManager,
    val cloudinaryProperties: CloudinaryProperties,
) {
    fun getDiaryList(
        userId: Long,
        startDate: LocalDate? = null,
        endDate: LocalDate? = null,
    ): List<GetDiaryListResponseDto> {
        val start = startDate ?: DateTool.Companion.getFirstDayOfCurrentMonth()
        val end = endDate ?: DateTool.Companion.getLastDayOfCurrentMonth()
        val diaries = diaryRepository.findByUserIdAndDateBetweenOrderByDateAsc(userId, start, end)
        return diaries.map {
            GetDiaryListResponseDto(diaryId = it.id, date = it.date, emotion = it.emotion)
        }
    }

    fun getDiary(
        userId: Long,
        diaryId: Long,
    ): GetDiaryResponseDto {
        val diary = getValidatedDiary(userId, diaryId)
        val imageUrl =
            diary.imageId?.let {
                imageManager.getImageUrl(it, 500, 300)
            }

        val diaryActivityCategories =
            diary.diaryActivityCategories.map {
                ActivityCategoryResponseDto(
                    id = it.activityCategory.id,
                    name = it.activityCategory.name,
                )
            }

        return GetDiaryResponseDto(
            diaryId = diary.id,
            date = diary.date,
            emotion = diary.emotion,
            imageUrl = imageUrl,
            content = diary.content,
            diaryActivityCategories = diaryActivityCategories,
        )
    }

    @Transactional
    fun createDiary(
        userId: Long,
        requestDto: CreateDiaryRequestDto,
    ): CreateDiaryResponseDto {
        val user = userRepository.findByIdOrNull(userId) ?: throw UserNotFoundException()
        val savedDiaries = diaryRepository.findDiariesByUserIdAndDate(userId, requestDto.date)
        if (savedDiaries.isNotEmpty()) {
            throw DiaryAlreadyExistsException(requestDto.date)
        }

        val image = handleImage(imageFile = requestDto.imageFile)

        val diary =
            diaryRepository.save(
                Diary(
                    user = user,
                    emotion = requestDto.emotion,
                    date = requestDto.date,
                    imageId = image?.publicId,
                    content = requestDto.content,
                ),
            )

        handleActivityCategory(activityCategoryIds = requestDto.activityCategoryIds, diary = diary)

        return CreateDiaryResponseDto(
            diaryId = diary.id,
            date = diary.date,
            emotion = diary.emotion,
            imageUrl = diary.imageId?.let { imageId -> imageManager.getImageUrl(imageId, 500, 300) },
            content = diary.content,
            diaryActivityCategories =
                diary.diaryActivityCategories.map { it.activityCategory }.toResponseDto(),
        )
    }

    @Transactional
    fun updateDiary(
        userId: Long,
        diaryId: Long,
        requestDto: UpdateDiaryRequestDto,
        imageFile: MultipartFile? = null,
    ): UpdateDiaryResponseDto {
        val diary = getValidatedDiary(userId, diaryId)
        val image =
            handleImage(imageFile = imageFile, deleteImage = requestDto.deleteImage, diary = diary)
        handleActivityCategory(activityCategoryIds = requestDto.activityCategoryIdList, diary = diary)

        diary.update(
            emotion = requestDto.emotion,
            content = requestDto.content,
            deleteImage = requestDto.deleteImage ?: false,
            imageId = image?.publicId,
        )

        return UpdateDiaryResponseDto(
            diaryId = diary.id,
            date = diary.date,
            emotion = diary.emotion,
            imageUrl = diary.imageId?.let { imageId -> imageManager.getImageUrl(imageId, 500, 300) },
            content = diary.content,
            diaryActivityCategories =
                diary.diaryActivityCategories.map { it.activityCategory }.toResponseDto(),
        )
    }

    private fun handleImage(
        imageFile: MultipartFile?,
        deleteImage: Boolean? = false,
        diary: Diary? = null,
    ): ImageUploadResult? {
        if (
            deleteImage == true ||
            imageFile != null
        ) {
            diary?.imageId?.let { imageId ->
                imageManager.deleteImage(imageId)
            }
        }

        return imageFile?.let {
            imageManager.uploadImage(
                file = it,
                uploadFolder = cloudinaryProperties.folder.diary,
                width = 500,
                height = 300,
            )
        }
    }

    private fun handleActivityCategory(
        activityCategoryIds: List<Long>?,
        diary: Diary,
    ) {
        val activityCategories =
            activityCategoryIds?.let {
                val activityCategories = activityCategoryRepository.findAllById(it)
                if (it.size != activityCategories.size) {
                    throw ActivityCategoryNotFoundException()
                }

                activityCategories
            }

        val diaryActivityCategories =
            activityCategories?.let {
                // TODO  diff 기반으로 추가/삭제만 처리하는 방식 고려
                diary.diaryActivityCategories.clear()
                diaryActivityCategoryRepository.flush()
                diaryActivityCategoryRepository.saveAll<DiaryActivityCategory>(
                    activityCategories.map {
                        DiaryActivityCategory(activityCategory = it, diary = diary)
                    },
                )
            }

        diaryActivityCategories?.let {
            diary.diaryActivityCategories.addAll(it)
        }
    }

    @Transactional
    fun deleteDiary(
        userId: Long,
        diaryId: Long,
    ) {
        val diary = getValidatedDiary(userId, diaryId)
        diary.imageId?.let { imageId ->
            imageManager.deleteImage(imageId)
        }

        diaryRepository.delete(diary)
    }

    private fun getValidatedDiary(
        userId: Long,
        diaryId: Long,
    ): Diary {
        val diary = diaryRepository.findByIdOrNull(diaryId) ?: throw DiaryNotFoundException()
        if (diary.user.id != userId) {
            throw NotDiaryOwnerException()
        }

        return diary
    }
}
