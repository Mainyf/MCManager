package io.github.mainyf.mc_manager.utils

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

private val servletRequestAttr = lazy { RequestContextHolder.getRequestAttributes() as ServletRequestAttributes }

fun currentRequest() = servletRequestAttr.value.request

fun currentResponse() = servletRequestAttr.value.response