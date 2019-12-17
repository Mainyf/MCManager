package io.github.mainyf.mc_manager.internal

import io.github.mainyf.mc_manager.CustomException
import io.github.mainyf.mc_manager.ErrorCause
import io.github.mainyf.mc_manager.entitys.JsonResult
import org.springframework.http.HttpStatus
import java.lang.Exception

internal fun <T : Any> runHttp(block: () -> T): JsonResult<T> {
    return try {
        JsonResult(null, block())
    } catch (ex: CustomException) {
        JsonResult(ex.errorCause, null, ex.message)
    } catch (ex: Exception) {
        JsonResult(ErrorCause.UNKOWN_ERROR, null, ErrorCause.UNKOWN_ERROR.message)
    }
}
