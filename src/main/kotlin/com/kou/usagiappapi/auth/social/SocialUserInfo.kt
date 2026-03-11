package com.kou.usagiappapi.auth.social

import com.kou.usagiappapi.user.enums.SocialProvider

data class SocialUserInfo(
    val provider: SocialProvider,
    val providerId: String,
    val email: String,
    val name: String?,
)
