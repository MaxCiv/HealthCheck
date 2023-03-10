package com.maxciv.healthcheck.domain

/**
 * @author maxim.oleynik
 * @since 17.08.2021
 */
enum class HealthCheckResult { //TODO 0 keep enums

    INITIAL,
    LOADING,
    SUCCESS,
    FAIL,
    NO_INTERNET,
    ;
}
