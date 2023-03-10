package com.maxciv.healthcheck.ui.main

import com.maxciv.healthcheck.common.state.Data
import com.maxciv.healthcheck.common.state.Error
import com.maxciv.healthcheck.common.state.Initial
import com.maxciv.healthcheck.common.state.LoadableValueState
import com.maxciv.healthcheck.common.state.Loading
import com.maxciv.healthcheck.common.viewmodel.CoroutineViewModel
import com.maxciv.healthcheck.domain.HealthCheckCollection
import com.maxciv.healthcheck.domain.HealthCheckData
import com.maxciv.healthcheck.domain.HealthCheckResult
import com.maxciv.healthcheck.repository.HealthCheckCollectionsRepository
import com.maxciv.healthcheck.service.HealthCheckResultData
import com.maxciv.healthcheck.service.HealthChecker
import com.maxciv.healthcheck.service.UrlReachabilityChecker
import com.maxciv.healthcheck.util.shit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * @author maxim.oleynik
 * @since 15.08.2021
 */
@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val urlReachabilityChecker: UrlReachabilityChecker,
    private val healthCheckCollectionsRepository: HealthCheckCollectionsRepository,
    private val healthChecker: HealthChecker,
    private val healthCheckCollectionListItemMapper: HealthCheckCollectionListItemMapper,
) : CoroutineViewModel() {

    val state = MainState()

    init {
        executeOnIO(cancelOnClear = true) {
            while (true) {
                state.isUrlReachable = urlReachabilityChecker.checkUrlReachability("http://52.15.88.104:8080/")
                delay(30_000L)
            }
        }
        executeOnIO(cancelOnClear = true) {
            combine(
                healthCheckCollectionsRepository.healthCheckCollectionsFlow,
                healthChecker.healthCheckResultsFlow,
                healthCheckCollectionListItemMapper::map,
            ).collect { healthCheckCollectionListItems ->
                state.healthCheckCollectionListItems = healthCheckCollectionListItems
                shit("state.healthCheckCollectionListItems = ${state.healthCheckCollectionListItems}")
            }
        }
    }
}

class HealthCheckCollectionListItemMapper
@Inject
constructor() {

    fun map(
        healthCheckCollections: List<HealthCheckCollection>,
        healthCheckIdToResult: Map<Long, LoadableValueState<HealthCheckResultData>>,
    ): List<HealthCheckCollectionListItem> {
        return healthCheckCollections.map { healthCheckCollection ->
            HealthCheckCollectionListItem(
                id = healthCheckCollection.id,
                name = healthCheckCollection.name,
                healthCheckListItems = healthCheckCollection.healthChecks.map { healthCheck ->
                    HealthCheckListItem(
                        id = healthCheck.id,
                        name = healthCheck.name,
                        url = healthCheck.url,
                        healthCheckResult = getHealthCheckResult(healthCheck, healthCheckIdToResult),
                    )
                }
            )
        }
    }

    private fun getHealthCheckResult(
        healthCheck: HealthCheckData,
        healthCheckIdToResult: Map<Long, LoadableValueState<HealthCheckResultData>>,
    ): HealthCheckResult {
        return when (val result = healthCheckIdToResult[healthCheck.id] ?: Initial) {
            Initial -> {
                HealthCheckResult.INITIAL
            }
            Loading -> {
                HealthCheckResult.LOADING
            }
            is Error -> {
                HealthCheckResult.FAIL
            }
            is Data -> {
                if (result.value.isSuccess) {
                    HealthCheckResult.SUCCESS
                } else {
                    HealthCheckResult.FAIL
                }
            }
        }
    }
}
