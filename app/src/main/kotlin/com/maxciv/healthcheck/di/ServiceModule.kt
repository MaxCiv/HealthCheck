package com.maxciv.healthcheck.di

import com.maxciv.healthcheck.service.OkHttpUrlReachabilityChecker
import com.maxciv.healthcheck.service.UrlReachabilityChecker
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * @author maxim.oleynik
 * @since 15.08.2021
 */
@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideUrlReachabilityChecker(
        okHttpClient: OkHttpClient,
    ): UrlReachabilityChecker {
        return OkHttpUrlReachabilityChecker(
            okHttpClient,
        )
    }
}
