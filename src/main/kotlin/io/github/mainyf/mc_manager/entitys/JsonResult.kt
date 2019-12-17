package io.github.mainyf.mc_manager.entitys

import io.github.mainyf.mc_manager.ErrorCause

data class JsonResult<T>(
    val errorCause: ErrorCause? = null,
    val data: T? = null,
    val errorMessage: String? = null
)