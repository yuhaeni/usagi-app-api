package security.oauth

import com.kou.kouappapi.enums.SocialProvider

interface OAuth2UserInfoExtractor {
    fun supports(provider: SocialProvider): Boolean

    fun extractUserInfo(attributes: Map<String, Any>): OAuth2UserInfo
}
