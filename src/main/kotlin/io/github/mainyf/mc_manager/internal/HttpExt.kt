package io.github.mainyf.mc_manager.internal

import io.github.mainyf.mc_manager.CustomException
import io.github.mainyf.mc_manager.ErrorCause
import io.github.mainyf.mc_manager.entitys.JsonResult
import io.github.mainyf.mc_manager.utils.currentResponse
import org.apache.logging.log4j.LogManager

private val LOG = LogManager.getLogger()

internal fun <T : Any?> runHttp(block: () -> T): JsonResult<T> {
//    currentResponse()?.status = 200
    return try {
        JsonResult(null, block())
    } catch (ex: CustomException) {
        LOG.error("error", ex)
        JsonResult(ex.errorCause, null, ex.message)
    } catch (ex: Exception) {
        LOG.error("error", ex)
        JsonResult(ErrorCause.UNKOWN_ERROR, null, ErrorCause.UNKOWN_ERROR.message)
    }
}