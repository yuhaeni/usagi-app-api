package security.oauth

import com.kou.kouappapi.enums.SocialProvider
import org.springframework.stereotype.Component

@Component
class GoogleUserInfoExtractor : OAuth2UserInfoExtractor {

    override fun supports(provider: SocialProvider): Boolean =
        provider == SocialProvider.GOOGLE

    override fun extractUserInfo(attributes: Map<String, Any>): OAuth2UserInfo {
        val providerId = attributes["sub"] as? String
            ?: throw MissingUserInfoException()

        val email = attributes["email"] as? String
            ?: throw MissingUserInfoException()

        if (attributes["email_verified"] as? Boolean != true) {
            throw UnverifiedEmailException()
        }

        return OAuth2UserInfo(
            email = email,
            socialProvider = SocialProvider.GOOGLE,
            providerId = providerId,
        )
    }

}
