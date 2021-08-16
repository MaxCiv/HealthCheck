package com.maxciv.healthcheck.service

import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * @author maxim.oleynik
 * @since 15.08.2021
 */
class OkHttpUrlReachabilityChecker
constructor(
    private val okHttpClient: OkHttpClient,
) : UrlReachabilityChecker {

    override fun checkUrlReachability(url: String): Boolean {
        val request = Request.Builder()
            .url(url)
            .method("GET", null)
            .build()
        val response = try {
            okHttpClient.newCall(request).execute()
        } catch (cause: Throwable) {
            return false
        }
        return response.isSuccessful
    }
}
