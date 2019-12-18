package io.github.mainyf.mc_manager.controllers

import io.github.mainyf.mc_manager.ErrorCause
import io.github.mainyf.mc_manager.annotations.AuthIgnore
import io.github.mainyf.mc_manager.config.Audience
import io.github.mainyf.mc_manager.entitys.IUser
import io.github.mainyf.mc_manager.internal.logger
import io.github.mainyf.mc_manager.internal.runHttp
import io.github.mainyf.mc_manager.toEx
import io.github.mainyf.mc_manager.utils.JwtHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth/*")
class AuthController {

    companion object {
        private var LOG = logger()
    }

    @Autowired
    private lateinit var audience: Audience

    @PostMapping("/login")
    @AuthIgnore
    fun login(@RequestBody payload: LoginPayload) = runHttp {
        if (payload.isEmpty()) throw ErrorCause.ARGUMENT_ERROR.toEx("账号或密码无效")
        if (!payload.isExists()) throw ErrorCause.ARGUMENT_ERROR.toEx("账号不存在")
        val user = payload.toUser()!!
        if (user.password != payload.password) throw ErrorCause.ARGUMENT_ERROR.toEx("密码不正确")
        val token = JwtHelper.createJWT(user.uniqueId, user.username, user.role, audience)
        LOG.info("Login success, token = {}", token)
        mapOf("token" to token)
    }

}

class LoginPayload(
    override var username: String = "",
    override var password: String = ""
) : IUser