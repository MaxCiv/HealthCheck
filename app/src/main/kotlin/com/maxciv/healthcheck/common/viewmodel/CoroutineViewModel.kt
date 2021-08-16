package com.maxciv.healthcheck.common.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author maxim.oleynik
 * @since 15.08.2021
 */
abstract class CoroutineViewModel : ViewModel() {

    protected var isInitialized: Boolean = false

    private val viewModelJob: CompletableJob = SupervisorJob()
    private val viewModelScope: CoroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val cancelableViewModelJob: CompletableJob = SupervisorJob()
    private val cancelableViewModelScope: CoroutineScope = CoroutineScope(cancelableViewModelJob + Dispatchers.Main)

    override fun onCleared() {
        cancelableViewModelJob.cancel()
        super.onCleared()
    }

    protected fun execute(cancelOnClear: Boolean = false, task: suspend CoroutineScope.() -> Unit): Job {
        return executeImpl(cancelOnClear, task)
    }

    protected fun executeOnIO(cancelOnClear: Boolean = false, task: suspend CoroutineScope.() -> Unit): Job {
        return executeImpl(cancelOnClear, task, context = Dispatchers.IO)
    }

    private fun executeImpl(
            cancelOnClear: Boolean,
            task: suspend CoroutineScope.() -> Unit,
            context: CoroutineContext = EmptyCoroutineContext,
    ): Job {
        val scope = if (cancelOnClear) {
            cancelableViewModelScope
        } else {
            viewModelScope
        }
        return scope.launch(context) {
            task.invoke(this)
        }
    }
}
