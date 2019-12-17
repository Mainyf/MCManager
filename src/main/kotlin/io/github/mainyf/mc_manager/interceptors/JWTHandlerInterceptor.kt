package io.github.mainyf.mc_manager.interceptors

import io.github.mainyf.mc_manager.CustomException
import io.github.mainyf.mc_manager.ErrorCause
import io.github.mainyf.mc_manager.annotations.AuthIgnore
import io.github.mainyf.mc_manager.config.Audience
import io.github.mainyf.mc_manager.internal.logger
import io.github.mainyf.mc_manager.utils.JwtHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JWTHandlerInterceptor : HandlerInterceptor {

    companion object {
        val LOG = logger()
    }

    @Autowired
    private lateinit var audience: Audience

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {

        // ignore json ignore annotation
        (handler as? HandlerMethod)?.let {
            val jwtIgnore = it.getMethodAnnotation(AuthIgnore::class.java)
            if (jwtIgnore != null) {
                return true
            }
        }

        if (HttpMethod.OPTIONS.equals(request.method)) {
            response.status = HttpServletResponse.SC_OK
            return true
        }

        val authHeader = request.getHeader(JwtHelper.AUTH_HEADER_KEY)
        if (!authHeader.startsWith(JwtHelper.TOKEN_PREFIX)) {
            LOG.info("### 用户未登录, 请先登录 ###")
            throw CustomException(ErrorCause.NOT_LOGGED_IN, "用户未登录, 请先登录")
        }
        val token = authHeader.substring(JwtHelper.TOKEN_PREFIX.length)
        JwtHelper.parseJWT(token, audience.base64SecretKey)
        return true
    }

}