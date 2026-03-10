package com.kou.usagiappapi.service

import com.kou.usagiappapi.repository.ActivityCategoryRepository
import com.kou.usagiappapi.service.dto.GetActivityCategoryListResponseDto
import org.springframework.stereotype.Service

@Service
class ActivityCategoryService(
    val activityCategoryRepository: ActivityCategoryRepository,
) {
    fun getActivityCategoryList(userId: Long): List<GetActivityCategoryListResponseDto> {
        val activityCategoryList = activityCategoryRepository.findByUserIdIsNullOrUserIdOrderByName(userId)
        val sortedActivityCategoryList = activityCategoryList.sortedBy { it.name }
        return sortedActivityCategoryList.map {
            GetActivityCategoryListResponseDto(
                activityCategoryId = it.id,
                name = it.name,
                userId = it.user?.id,
            )
        }
    }
}
