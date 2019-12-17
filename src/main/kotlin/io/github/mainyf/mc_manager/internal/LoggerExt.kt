package io.github.mainyf.mc_manager.internal

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal class LoggerDelegate : ReadOnlyProperty<Any?, Logger> {

    private var logger: Logger? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): Logger {
        if (logger == null) {
            logger = LoggerFactory.getLogger(thisRef!!.javaClass)
        }
        return logger!!
    }
}

internal fun loggerDe(): LoggerDelegate = LoggerDelegate()

//inline fun <reified T> logger(): Logger {
//    return LoggerFactory.getLogger(T::class.java)
//}

internal inline fun <reified T> T.logger(): Logger {
    val kc = T::class
    if (kc.isCompanion) {
        return LoggerFactory.getLogger(kc.java.enclosingClass)
    }
    return LoggerFactory.getLogger(kc.java)
}