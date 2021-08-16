package com.maxciv.healthcheck.di

import com.maxciv.healthcheck.util.isDebug
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author maxim.oleynik
 * @since 15.08.2021
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder()
            .connectTimeout(HTTP_CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .apply {
                if (isDebug) {
                    addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
            }
            .build()
    }

    private const val HTTP_CONNECTION_TIMEOUT_MS: Long = 20_000L
}
