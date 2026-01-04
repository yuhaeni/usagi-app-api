package com.kou.kouappapi.enums

import org.springframework.security.oauth2.core.OAuth2AuthenticationException

enum class SocialProvider(
    val registrationId: String,
) {
    GOOGLE("google"),
    APPLE("apple"),
    ;

    companion object {
        fun findByRegistrationId(registrationId: String): SocialProvider =
            entries.find { it.registrationId == registrationId }
                ?: throw OAuth2AuthenticationException("Unsupported registrationId: $registrationId")
    }
}
