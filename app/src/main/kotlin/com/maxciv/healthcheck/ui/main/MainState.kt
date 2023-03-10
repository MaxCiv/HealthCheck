package com.maxciv.healthcheck.ui.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * @author maxim.oleynik
 * @since 15.08.2021
 */
class MainState {
    
    private val isUrlReachableMutableFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isUrlReachableFlow: StateFlow<Boolean> = isUrlReachableMutableFlow
    var isUrlReachable: Boolean
        get() = isUrlReachableMutableFlow.value
        set(value) {
            isUrlReachableMutableFlow.value = value
        }
    
    private val healthCheckCollectionListItemsMutableFlow: MutableStateFlow<List<HealthCheckCollectionListItem>> = MutableStateFlow(listOf())
    val healthCheckCollectionListItemsFlow: StateFlow<List<HealthCheckCollectionListItem>> = healthCheckCollectionListItemsMutableFlow
    var healthCheckCollectionListItems: List<HealthCheckCollectionListItem>
        get() = healthCheckCollectionListItemsMutableFlow.value
        set(value) {
            healthCheckCollectionListItemsMutableFlow.value = value
        }
}
