package io.github.mainyf.mc_manager

import io.github.mainyf.common_lib.exts.fields
import io.github.mainyf.mc_manager.config.Audience
import io.github.mainyf.mc_manager.config.BeanTools
import io.github.mainyf.mc_manager.services.UserService

internal val audienceLazy = lazy { BeanTools.getBean<Audience>() }

internal val userServiceLazy = lazy { BeanTools.getBean<UserService>() }

fun Any.toMap(vararg exclude: String): Map<String, Any> {
    val clazz = javaClass
    return mutableMapOf<String, Any>().apply {
        clazz.fields().forEach {
            val name = it.name
            if (exclude.contains(name) || name == "Companion") {
                return@forEach
            }
            it.isAccessible = true
            this[name] = it.get(this@toMap)
        }
    }
}