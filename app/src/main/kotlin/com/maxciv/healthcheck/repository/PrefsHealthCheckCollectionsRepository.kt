package com.maxciv.healthcheck.repository

import com.maxciv.healthcheck.domain.HealthCheckCollection
import com.maxciv.healthcheck.prefs.HealthChecksPrefs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * @author maxim.oleynik
 * @since 17.08.2021
 */
class PrefsHealthCheckCollectionsRepository
constructor(
    private val healthChecksPrefs: HealthChecksPrefs,
) : HealthCheckCollectionsRepository {

    private val healthCheckCollectionsMutableFlow: MutableStateFlow<List<HealthCheckCollection>> = MutableStateFlow(healthChecksPrefs.healthCheckCollections)
    override val healthCheckCollectionsFlow: Flow<List<HealthCheckCollection>> = healthCheckCollectionsMutableFlow
    override var healthCheckCollections: List<HealthCheckCollection>
        get() = healthCheckCollectionsMutableFlow.value
        set(value) {
            healthChecksPrefs.healthCheckCollections = value
            healthCheckCollectionsMutableFlow.value = value
        }
}
