package com.maxciv.healthcheck

import android.app.Application
import com.maxciv.healthcheck.util.isDebug
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * @author maxim.oleynik
 * @since 15.08.2021
 */
@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (isDebug) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
