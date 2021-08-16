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
}
