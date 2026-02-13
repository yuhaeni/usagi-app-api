package com.kou.kouappapi.service.dto

import com.kou.kouappapi.enums.CoupleStatus

data class GetUserProfileResponseDto(
    val userId: Long,
    val name: String? = null,
    val email: String,
    var profileImageUrl: String? = null,
    val coupleId: Long? = null,
    val coupleStatus: CoupleStatus? = null,
)
