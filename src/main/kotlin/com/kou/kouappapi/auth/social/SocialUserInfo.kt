package com.kou.kouappapi.auth.social

import com.kou.kouappapi.enums.SocialProvider

data class SocialUserInfo(
    val provider: SocialProvider,
    val providerId: String,
    val email: String,
    val name: String?,
)
