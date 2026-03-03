package com.kou.kouappapi.service

import com.kou.kouappapi.repository.ActivityCategoryRepository
import com.kou.kouappapi.service.dto.GetActivityCategoryListResponseDto
import org.springframework.stereotype.Service

@Service
class ActivityCategoryService(
    val activityCategoryRepository: ActivityCategoryRepository,
) {
    fun getActivityCategoryList(userId: Long): List<GetActivityCategoryListResponseDto> {
        val activityCategoryList = activityCategoryRepository.findByUserIdIsNullOrUserIdOrderByName(userId)
        return activityCategoryList.map {
            GetActivityCategoryListResponseDto(
                name = it.name,
                userId = it.user?.id,
            )
        }
    }
}
