package com.kou.usagiappapi.activityCategory.controller.dto

data class GetActivityCategoryListResponse(
    val activityCategoryId: Long,
    val name: String,
    val userId: Long? = 0L,
)
