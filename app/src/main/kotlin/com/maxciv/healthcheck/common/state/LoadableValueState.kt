package com.maxciv.healthcheck.common.state

/**
 * @author maxim.oleynik
 * @since 17.08.2021
 */
sealed class LoadableValueState<out VALUE : Any>

object Initial : LoadableValueState<Nothing>()

object Loading : LoadableValueState<Nothing>()

data class Error
constructor(
    val cause: Throwable?,
) : LoadableValueState<Nothing>()

data class Data<out VALUE : Any>
constructor(
    val value: VALUE,
) : LoadableValueState<VALUE>()
