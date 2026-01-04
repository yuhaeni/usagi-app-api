package security.oauth

import com.kou.kouappapi.entity.User
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User

class OAuth2UserPrincipalService(
    private val userRepository: UserRepository,
    private val extractors: List<OAuth2UserInfoExtractor>,
) : DefaultOAuth2UserService() {
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)
        val provider = extractProvider(userRequest)

        val extractor =
            extractors.find { it.supports(provider) }
                ?: throw UnsupportedProviderException()

        val userInfo = extractor.extractUserInfo(oAuth2User.attributes)
        val user = findOrCreateUser(provider, userInfo)
        return OAuth2UserPrincipal(user, oAuth2User.attributes)
    }

    private fun findOrCreateUser(
        provider: SocialProvider,
        userInfo: OAuth2UserInfo,
    ): User {
        val userByProvider = userRepository.findByProviderAndEmail(provider, userInfo.email)
        if (userByProvider != null) {
            logger.debug("Existing user logged in: ${userByProvider.id}")
            return userByProvider
        }

        val userByEmail = userRepository.findByEmail(userInfo.email)
        if (userByEmail != null) {
            throw EmailAlreadyExistsException()
        }

        val saveUser =
            userRepository.save(
                User(
                    email = userInfo.email,
                    provider = provider,
                    providerId = userInfo.providerId,
                ),
            )

        return saveUser
    }

    private fun extractProvider(userRequest: OAuth2UserRequest): SocialProvider {
        val registrationId = userRequest.clientRegistration.registrationId
        return SocialProvider.findByRegistrationId(registrationId)
    }
}
