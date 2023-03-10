package com.maxciv.healthcheck.repository

import com.maxciv.healthcheck.domain.HealthCheckCollection
import kotlinx.coroutines.flow.Flow

/**
 * @author maxim.oleynik
 * @since 17.08.2021
 */
interface HealthCheckCollectionsRepository {

    val healthCheckCollectionsFlow: Flow<List<HealthCheckCollection>>
    var healthCheckCollections: List<HealthCheckCollection>
}
