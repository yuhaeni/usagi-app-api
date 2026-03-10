package com.kou.usagiappapi.auth.social

import com.kou.usagiappapi.enums.SocialProvider

interface SocialAuthStrategy {
    fun getSocialProvider(): SocialProvider

    fun authenticate(token: String): SocialUserInfo
}
