package io.github.mainyf.mc_manager.entitys

import io.github.mainyf.mc_manager.INIT_UUID
import java.util.*

class MCServer(
    val displayName: String = "",
    val serverPath: String = "",
    val serverEnginePath: String = "",
    val jvmArg: String = "",
    val creater: UUID = INIT_UUID,
    val createTime: Date = Date()
)
