@file:Suppress("UNUSED_VARIABLE")

package io.github.mainyf.mc_manager.utils

import io.github.mainyf.common_lib.exts.asUUID
import io.github.mainyf.common_lib.exts.currentTime
import io.github.mainyf.common_lib.exts.toDate
import io.github.mainyf.mc_manager.CustomException
import io.github.mainyf.mc_manager.ErrorCause
import io.github.mainyf.mc_manager.audienceLazy
import io.github.mainyf.mc_manager.config.Audience
import io.github.mainyf.mc_manager.config.BeanTools
import io.github.mainyf.mc_manager.entitys.User
import io.github.mainyf.mc_manager.entitys.UserRole
import io.github.mainyf.mc_manager.internal.logger
import io.github.mainyf.mc_manager.services.UserService
import io.github.mainyf.mc_manager.userServiceLazy
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.apache.commons.codec.binary.Base64
import java.util.*
import javax.crypto.spec.SecretKeySpec
import javax.servlet.http.HttpServletRequest

object JwtHelper {

    private val LOG = logger()
    const val AUTH_HEADER_KEY = "Authorization"
    const val TOKEN_PREFIX = "Bearer "

    fun parseJWT(token: String, base64SecretKey: String): Claims {
        try {
            return Jwts.parser()
                .setSigningKey(Base64.decodeBase64(base64SecretKey))
                .parseClaimsJws(token)
                .body
        } catch (ex: ExpiredJwtException) {
            // token expired
            LOG.info("token expired.")
            LOG.error("token expired.", ex)
            throw CustomException(ErrorCause.NOT_LOGGED_IN, "Login time expired, please re login")
        } catch (ex: Exception) {
            // json parse error
            LOG.info("create token error.")
            LOG.error("create token error.", ex)
            throw CustomException(ErrorCause.UNKOWN_ERROR)
        }
    }

    fun createJWT(
        userId: UUID,
        username: String,
        role: List<UserRole>,
        audience: Audience
    ): String {
        try {
            val signatureAlgorithm = SignatureAlgorithm.HS256
            val nowMillis = currentTime()
            val now = nowMillis.toDate()

            val apiKeySecretBytes = Base64.decodeBase64(audience.base64SecretKey)
            val signingKey = SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.jcaName)
            val encryId = Base64.encodeBase64(userId.toString().toByteArray())
            return Jwts.builder().also { builder ->
                builder.setHeaderParam("typ", "JWT")
                    .claim("role", role)
                    .claim("userId", userId)
                    .setSubject(username)
                    .setIssuer(audience.clientId)
                    .setIssuedAt(Date())
                    .setAudience(audience.name)
                    .signWith(signingKey, signatureAlgorithm)

                // set expiresTime
                audience.expiresTime.run {
                    if (this >= 0) {
                        val expMillis = nowMillis + this
                        builder.setExpiration(expMillis.toDate()).setNotBefore(now)
                    }
                }
            }.compact()
        } catch (ex: Exception) {
            // signing name fail
            LOG.info("create token error.")
            LOG.error("create token error.", ex)
            throw CustomException(ErrorCause.UNKOWN_ERROR)
        }
    }

    fun getUsername(token: String, base64SecretKey: String): String {
        return parseJWT(token, base64SecretKey).subject
    }

    fun getUserId(token: String, base64SecretKey: String): UUID {
        val claims = parseJWT(token, base64SecretKey)
        val userId = claims.get("userId", String::class.java)
        return userId.asUUID()
    }

    fun isExpiration(token: String, base64SecretKey: String): Boolean {
        return parseJWT(token, base64SecretKey).let {
            val now = currentTime()
            it.expiration.before(Date())
        }
    }

}

internal fun String.toUser(): User? {
    val userId = JwtHelper.getUserId(this, audienceLazy.value.base64SecretKey)
    return userServiceLazy.value.getUser(userId)
}

internal fun HttpServletRequest.getUserByToken(): User? {
    return getToken()?.toUser()
}

internal fun HttpServletRequest.getToken(): String? {
    return getHeader(JwtHelper.AUTH_HEADER_KEY)?.substring(JwtHelper.TOKEN_PREFIX.length)
}