package io.github.mainyf.mc_manager.controllers

import io.github.mainyf.mc_manager.entitys.MCServer
import io.github.mainyf.mc_manager.internal.logger
import io.github.mainyf.mc_manager.internal.runHttp
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/mcserver/*")
class MCServerController {

    companion object {
        private var LOG = logger()
    }

    @PostMapping
    fun create(@RequestBody payload: MCServer) = runHttp {
        
    }

}