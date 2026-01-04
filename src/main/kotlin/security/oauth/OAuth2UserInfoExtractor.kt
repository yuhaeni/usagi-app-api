package security.oauth

import com.kou.kouappapi.enums.SocialProvider
import org.springframework.security.oauth2.core.user.OAuth2User

interface OAuth2UserInfoExtractor {
    fun supports(provider: SocialProvider): Boolean
    fun extractUserInfo(attributes: Map<String, Any>): OAuth2UserInfo
}
