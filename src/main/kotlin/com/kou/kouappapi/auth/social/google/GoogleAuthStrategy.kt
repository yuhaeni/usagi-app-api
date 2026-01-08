package com.kou.kouappapi.auth.social.google

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.kou.kouappapi.auth.social.SocialAuthStrategy
import com.kou.kouappapi.auth.social.SocialUserInfo
import com.kou.kouappapi.enums.SocialProvider
import com.kou.kouappapi.exception.AuthEmailNotVerifiedException
import com.kou.kouappapi.exception.AuthInvalidIdTokenException
import org.springframework.stereotype.Component

@Component
class GoogleAuthStrategy(
    private val googleProperties: GoogleProperties,
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
                listOf(
                    googleProperties.clientId,
                    googleProperties.playground, // TODO 삭제 필요
                ),
            ).build()
}
