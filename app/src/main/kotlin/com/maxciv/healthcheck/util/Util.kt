package com.maxciv.healthcheck.util

import com.maxciv.healthcheck.BuildConfig
import okhttp3.internal.format
import timber.log.Timber
import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

/**
 * @author maxim.oleynik
 * @since 15.08.2021
 */
val isDebug: Boolean
    get() = BuildConfig.DEBUG

val isNotDebug: Boolean
    get() = !isDebug

inline fun shit(text: String) {
    Timber.e("SHIT $text")
}

@OptIn(ExperimentalTime::class)
inline fun <R> measureMy(borderMs: Long = 1, text: String = "", crossinline block: () -> R): R {
    val returnValue: R
    val measuredTime = measureTime { returnValue = block.invoke() }
    val microseconds = measuredTime.inWholeMicroseconds
    val ms = microseconds / 1000L
    val mcsSuffix = microseconds - (ms * 1000L)
    if (ms >= borderMs) {
        shit("$text = ${format("%2d,%03d", ms, mcsSuffix)} mcs")
    }
    return returnValue
}

fun randomId(): Long {
    val id = UUID.randomUUID().mostSignificantBits
    return when {
        id < (Long.MIN_VALUE + 100) -> randomId()
        else -> id
    }
}
