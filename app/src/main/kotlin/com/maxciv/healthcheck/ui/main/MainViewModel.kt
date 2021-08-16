package com.maxciv.healthcheck.ui.main

import com.maxciv.healthcheck.common.viewmodel.CoroutineViewModel
import com.maxciv.healthcheck.service.UrlReachabilityChecker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
) : CoroutineViewModel() {

    val state = MainState()

    init {
        executeOnIO(cancelOnClear = true) {
            while (true) {
                state.isUrlReachable = urlReachabilityChecker.checkUrlReachability("http://52.15.88.104:8080/")
                delay(30_000L)
            }
        }
    }
}
