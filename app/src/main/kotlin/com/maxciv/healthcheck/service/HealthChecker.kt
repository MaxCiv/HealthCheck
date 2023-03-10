package com.maxciv.healthcheck.service

import com.maxciv.healthcheck.common.state.Data
import com.maxciv.healthcheck.common.state.LoadableValueState
import com.maxciv.healthcheck.common.state.Loading
import com.maxciv.healthcheck.domain.HealthCheckCollection
import com.maxciv.healthcheck.domain.HealthCheckData
import com.maxciv.healthcheck.repository.HealthCheckCollectionsRepository
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * @author maxim.oleynik
 * @since 17.08.2021
 */
interface HealthChecker {

    val healthCheckResultsFlow: Flow<Map<Long, LoadableValueState<HealthCheckResultData>>>
    val healthCheckResults: Map<Long, LoadableValueState<HealthCheckResultData>>
}

/**
 * @author maxim.oleynik
 * @since 17.08.2021
 */
class HealthCheckerImpl
constructor(
    private val healthCheckCollectionsRepository: HealthCheckCollectionsRepository,
    private val urlReachabilityChecker: UrlReachabilityChecker,
) : HealthChecker {

    private val job: CompletableJob = SupervisorJob()
    private val coroutineScope: CoroutineScope = CoroutineScope(job + Dispatchers.IO)

    private val results: MutableMap<Long, LoadableValueState<HealthCheckResultData>> = mutableMapOf()
    private val healthCheckJobs: MutableMap<Long, Job> = mutableMapOf()

    private val healthCheckResultsMutableFlow: MutableStateFlow<Map<Long, LoadableValueState<HealthCheckResultData>>> = MutableStateFlow(mapOf())
    override val healthCheckResultsFlow: Flow<Map<Long, LoadableValueState<HealthCheckResultData>>> = healthCheckResultsMutableFlow
    override var healthCheckResults: Map<Long, LoadableValueState<HealthCheckResultData>>
        get() = healthCheckResultsMutableFlow.value
        private set(value) {
            healthCheckResultsMutableFlow.value = value
        }

    init {
        coroutineScope.launch {
            healthCheckCollectionsRepository.healthCheckCollectionsFlow.collect { healthCheckCollections: List<HealthCheckCollection> ->
                healthCheckJobs.values.forEach(Job::cancel)

                healthCheckCollections.forEach { healthCheckCollection ->
                    healthCheckCollection.healthChecks.forEach { healthCheck ->
                        healthCheckJobs[healthCheck.id] = launchHealthCheckJob(healthCheckCollection, healthCheck)
                    }
                }
            }
        }
    }

    private fun launchHealthCheckJob(
        healthCheckCollection: HealthCheckCollection,
        healthCheckData: HealthCheckData,
    ): Job {
        return coroutineScope.launch {
            while (true) {
                setHealthCheckResult(healthCheckData.id, Loading)

                val isReachable = urlReachabilityChecker.checkUrlReachability(healthCheckData.url)

                delay(300L)
                setHealthCheckResult(healthCheckData.id, Data(HealthCheckResultData(isReachable)))

                delay(30_000L) //TODO 0
            }
        }
    }

    private fun setHealthCheckResult(id: Long, value: LoadableValueState<HealthCheckResultData>) {
        synchronized(this) {
            results[id] = value
            healthCheckResults = results.toMap()
        }
    }
}

/**
 * @author maxim.oleynik
 * @since 17.08.2021
 */
data class HealthCheckResultData
constructor(
    val isSuccess: Boolean,
    val timeMs: Long = System.currentTimeMillis(),
)
