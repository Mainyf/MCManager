package io.github.mainyf.mc_manager.controllers

import io.github.mainyf.mc_manager.config.Audience
import io.github.mainyf.mc_manager.internal.logger
import io.github.mainyf.mc_manager.internal.runHttp
import io.github.mainyf.mc_manager.services.UserService
import io.github.mainyf.mc_manager.toMap
import io.github.mainyf.mc_manager.utils.getUserByToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/user/*")
class UserController {

    companion object {
        private var LOG = logger()
    }

    @Autowired
    private lateinit var audience: Audience

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var request: HttpServletRequest

    @GetMapping
    fun currentUser() = runHttp {
        request.getUserByToken()?.toMap("password", "EMPTY_UUID")
    }

}