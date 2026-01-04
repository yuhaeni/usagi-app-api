package security.oauth

import com.kou.kouappapi.enums.SocialProvider

data class OAuth2UserInfo(
    val email: String,
    val socialProvider: SocialProvider,
    val providerId: String,
)
