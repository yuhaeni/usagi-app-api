package com.kou.kouappapi.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "cloudinary")
data class CloudinaryProperties(
    var cloudName: String = "",
    var apiKey: String = "",
    var apiSecret: String = "",
    var folder: Folder = Folder(),
) {
    data class Folder(
        var profile: String = "",
        var diary: String = "",
    )
}
