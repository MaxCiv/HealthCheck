package com.maxciv.healthcheck.di

import com.maxciv.healthcheck.prefs.HealthChecksPrefs
import com.maxciv.healthcheck.repository.HealthCheckCollectionsRepository
import com.maxciv.healthcheck.repository.PrefsHealthCheckCollectionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @author maxim.oleynik
 * @since 17.08.2021
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHealthCheckCollectionsRepository(
        healthChecksPrefs: HealthChecksPrefs,
    ): HealthCheckCollectionsRepository {
        return PrefsHealthCheckCollectionsRepository(
            healthChecksPrefs,
        )
    }
}
