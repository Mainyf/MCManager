package io.github.mainyf.mc_manager.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "audience")
@Component
data class Audience(
    var clientId: String = "",
    var base64SecretKey: String = "",
    var name: String = "",
    var expiresTime: Long = -1L
)