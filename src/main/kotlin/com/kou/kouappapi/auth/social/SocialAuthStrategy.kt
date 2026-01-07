package com.kou.kouappapi.auth.social

import com.kou.kouappapi.enums.SocialProvider

interface SocialAuthStrategy {
    fun getSocialProvider(): SocialProvider

    fun authenticate(token: String): SocialUserInfo
}
