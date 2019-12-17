package io.github.mainyf.mc_manager.config

import io.github.mainyf.common_lib.exts.asUUIDFromByte
import io.github.mainyf.mc_manager.entitys.User
import io.github.mainyf.mc_manager.entitys.UserRole
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "user.root")
@Component
class DefaultAdminUser : User(role = mutableListOf(UserRole.ADMIN)) {
    init {
        uniqueId = "Default_Admin".asUUIDFromByte()
    }
}
