package com.kou.kouappapi.auth.social

import com.kou.kouappapi.auth.exception.AuthProviderNotSupportedException
import com.kou.kouappapi.enums.SocialProvider
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
