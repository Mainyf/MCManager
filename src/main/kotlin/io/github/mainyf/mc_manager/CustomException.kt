package io.github.mainyf.mc_manager

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

class CustomException(val errorCause: ErrorCause, val errorMessage: String? = null) : Exception() {
    override val message: String? get() = errorMessage ?: errorCause.message
}

enum class ErrorCause(val code: Int, val message: String) {
    UNKOWN_ERROR(8000, "未知错误。"),
    NOT_LOGGED_IN(8001, "用户未登录。"),
    ARGUMENT_ERROR(8002, "参数错误。");

    @JsonCreator
    fun getEnum(code: Int): ErrorCause? {
        return values().find { it.code == code }
    }

    @JsonValue
    fun getCodeValue(): Int = code

}

fun customEx(errorCause: ErrorCause, errorMessage: String) = CustomException(errorCause, errorMessage)

fun customEx(errorCause: ErrorCause) = CustomException(errorCause)

fun ErrorCause.toEx() = CustomException(this)

fun ErrorCause.toEx(errorMessage: String) = CustomException(this, errorMessage)
