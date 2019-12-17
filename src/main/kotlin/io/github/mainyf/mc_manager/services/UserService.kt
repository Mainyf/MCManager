package io.github.mainyf.mc_manager.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.mainyf.common_lib.exts.toJson
import io.github.mainyf.mc_manager.ErrorCause
import io.github.mainyf.mc_manager.config.DefaultAdminUser
import io.github.mainyf.mc_manager.entitys.User
import io.github.mainyf.mc_manager.internal.logger
import io.github.mainyf.mc_manager.toEx
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class UserService {

    private final val rootDir: File = File("")
    private final val userMap = ConcurrentHashMap<UUID, User>()
    private final val userFile = rootDir.resolve("users.json")
    private final val mapper = ObjectMapper().registerKotlinModule()

    @Autowired
    private lateinit var adminUser: DefaultAdminUser

    companion object {
        val LOG = logger()
    }

    init {
        if (userFile.exists()) {
            mapper.readValue<List<User>>(userFile.readText()).forEach { userMap[it.uniqueId] = it }
        } else {
            userFile.createNewFile()
        }
    }

    fun addUser(user: User, hasForce: Boolean = false) {
        if (!hasForce && user.isEmpty() || userMap.containsKey(user.uniqueId)) throw ErrorCause.ARGUMENT_ERROR.toEx()
        userMap[user.uniqueId] = user
        saveToFile()
    }

    fun delUser(user: User) {
        if (user.isEmpty() || !userMap.containsKey(user.uniqueId)) throw ErrorCause.ARGUMENT_ERROR.toEx()
        userMap.remove(user.uniqueId)
        saveToFile()
    }

    fun delUser(uniqueId: UUID) {
        if (!userMap.containsKey(uniqueId)) throw ErrorCause.ARGUMENT_ERROR.toEx()
        userMap.remove(uniqueId)
        saveToFile()
    }

    fun getUser(uniqueId: UUID): User {
        if (uniqueId == adminUser.uniqueId) return adminUser
        if (!userMap.containsKey(uniqueId)) throw ErrorCause.ARGUMENT_ERROR.toEx()
        return userMap[uniqueId]!!
    }

    @Scheduled(fixedRate = 50000, initialDelay = 50000)
    fun saveToFile() {
        LOG.info("Start User Data")
        userFile.writeText(userMap.values.toMutableList().apply { add(adminUser) }.toJson())
        LOG.info("Saved.")
    }


}