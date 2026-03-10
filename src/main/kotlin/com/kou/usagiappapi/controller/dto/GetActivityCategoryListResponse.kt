package com.kou.usagiappapi.controller.dto

data class GetActivityCategoryListResponse(
    val activityCategoryId: Long,
    val name: String,
    val userId: Long? = 0L,
)
