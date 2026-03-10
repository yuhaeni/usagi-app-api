package com.kou.usagiappapi.auth.social.google

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.kou.usagiappapi.auth.exception.AuthEmailNotVerifiedException
import com.kou.usagiappapi.auth.exception.AuthInvalidIdTokenException
import com.kou.usagiappapi.auth.social.SocialAuthStrategy
import com.kou.usagiappapi.auth.social.SocialUserInfo
import com.kou.usagiappapi.enums.SocialProvider
import org.springframework.core.env.Environment
import org.springframework.core.env.Profiles
import org.springframework.stereotype.Component

@Component
class GoogleAuthStrategy(
    private val googleProperties: GoogleProperties,
    private val environment: Environment,
) : SocialAuthStrategy {
    override fun getSocialProvider() = SocialProvider.GOOGLE

    override fun authenticate(token: String): SocialUserInfo {
        val payload =
            runCatching {
                verifier.verify(token)
            }.getOrElse { throw AuthInvalidIdTokenException() }
                ?.payload
                ?: throw AuthInvalidIdTokenException()

        if (!payload.emailVerified) {
            throw AuthEmailNotVerifiedException()
        }

        return SocialUserInfo(
            provider = SocialProvider.GOOGLE,
            providerId = payload.subject,
            email = payload.email,
            name = payload["name"] as? String ?: "",
        )
    }

    private val verifier: GoogleIdTokenVerifier =
        GoogleIdTokenVerifier
            .Builder(
                NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
            ).setAudience(
                buildList {
                    add(googleProperties.webClientId)
                    add(googleProperties.iosClientId)
                    if (environment.acceptsProfiles(Profiles.of("local | test"))) {
                        googleProperties.playground?.let { add(it) }
                    }
                },
            ).build()
}
