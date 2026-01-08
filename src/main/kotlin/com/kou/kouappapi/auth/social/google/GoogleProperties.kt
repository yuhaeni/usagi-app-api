package com.kou.kouappapi.auth.social.google

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "google.oauth2")
data class GoogleProperties(
    var clientId: String = "",
    var playground: String = "407408718192.apps.googleusercontent.com", // TODO 삭제필요
)
