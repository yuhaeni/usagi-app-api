package com.kou.usagiappapi.auth.social

import com.kou.usagiappapi.auth.exception.AuthProviderNotSupportedException
import com.kou.usagiappapi.user.enums.SocialProvider
import org.springframework.stereotype.Component

@Component
class SocialAuthStrategyFactory(
    strategies: List<SocialAuthStrategy>,
) {
    private val strategyMap: Map<SocialProvider, SocialAuthStrategy> =
        strategies.associateBy { it.getSocialProvider() }

    fun getStrategy(provider: SocialProvider): SocialAuthStrategy =
        strategyMap[provider]
            ?: throw AuthProviderNotSupportedException()
}
