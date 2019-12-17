package io.github.mainyf.mc_manager.controllers

import io.github.mainyf.mc_manager.ErrorCause
import io.github.mainyf.mc_manager.annotations.AuthIgnore
import io.github.mainyf.mc_manager.config.Audience
import io.github.mainyf.mc_manager.config.DefaultAdminUser
import io.github.mainyf.mc_manager.entitys.User
import io.github.mainyf.mc_manager.internal.logger
import io.github.mainyf.mc_manager.internal.runHttp
import io.github.mainyf.mc_manager.services.UserService
import io.github.mainyf.mc_manager.toEx
import io.github.mainyf.mc_manager.utils.JwtHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth/*")
class AuthController {

    companion object {
        private var LOG = logger()
    }

    @Autowired
    private lateinit var audience: Audience

    @Autowired
    private lateinit var adminUser: DefaultAdminUser

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/login")
    @AuthIgnore
    fun login(@RequestBody payload: LoginPayload) = runHttp {
        if (payload.isEmpty()) throw ErrorCause.ARGUMENT_ERROR.toEx()
        val token = JwtHelper.createJWT(payload.uniqueId, payload.username, payload.role, audience)
        LOG.info("Login success, token = {}", token)
        mapOf("token" to token)
    }

}

class LoginPayload : User()