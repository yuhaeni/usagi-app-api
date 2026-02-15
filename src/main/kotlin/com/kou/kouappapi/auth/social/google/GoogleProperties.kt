package com.kou.kouappapi.auth.social.google

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "google.oauth2")
data class GoogleProperties(
    var webClientId: String = "",
    var iosClientId: String = "",
    var playground: String? = null,
)
