package com.maxciv.healthcheck.service

/**
 * @author maxim.oleynik
 * @since 15.08.2021
 */
interface UrlReachabilityChecker {

    fun checkUrlReachability(url: String): Boolean
}
