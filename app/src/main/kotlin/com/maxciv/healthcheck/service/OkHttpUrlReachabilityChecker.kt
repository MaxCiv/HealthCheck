package com.maxciv.healthcheck.service

import okhttp3.OkHttpClient
import okhttp3.Request
import ru.gildor.coroutines.okhttp.await

/**
 * @author maxim.oleynik
 * @since 15.08.2021
 */
class OkHttpUrlReachabilityChecker
constructor(
    private val okHttpClient: OkHttpClient,
) : UrlReachabilityChecker {

    override suspend fun checkUrlReachability(url: String): Boolean {
        val request = Request.Builder()
            .url(url)
            .method("GET", null)
            .build()
        val response = try {
            okHttpClient.newCall(request).await()
        } catch (cause: Throwable) {
            return false
        }
        return response.isSuccessful
    }
}
