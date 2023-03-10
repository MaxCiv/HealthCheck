package com.maxciv.healthcheck.prefs

import android.content.Context
import android.content.getStringSafe
import androidx.core.content.edit
import com.maxciv.healthcheck.R
import com.maxciv.healthcheck.domain.HealthCheckCollection
import com.maxciv.healthcheck.domain.HealthCheckData
import com.squareup.moshi.fromJsonToList
import com.squareup.moshi.toJson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author maxim.oleynik
 * @since 16.08.2021
 */
@Singleton
class HealthChecksPrefs
@Inject
constructor(
    @ApplicationContext context: Context,
) : BasePrefs(context, FILE_NAME) {

    private val defaultHealthCheckCollectionsJson: String by lazy {
        listOf(
            HealthCheckCollection(
                id = 1L,
                name = context.getString(R.string.my_collection),
                healthChecks = listOf(
                    HealthCheckData(
                        id = 2L,
                        name = context.getString(R.string.google),
                        url = "https://www.google.com/",
                    ),
                    HealthCheckData(
                        id = 3L,
                        name = "MaxCiv Leaderboard Prod",
                        url = "http://52.15.88.104:8080/",
                    ),
                ),
            ),
            HealthCheckCollection(
                id = 2L,
                name = context.getString(R.string.my_collection),
                healthChecks = listOf(
                    HealthCheckData(
                        id = 4L,
                        name = context.getString(R.string.google),
                        url = "https://www.google.com/",
                    ),
                ),
            ),
        ).toJson()
    }

    private var _healthCheckCollections: List<HealthCheckCollection>? = null
    var healthCheckCollections: List<HealthCheckCollection>
        get() = _healthCheckCollections
            ?: prefs.getStringSafe(KEY_HEALTH_CHECK_COLLECTIONS, defaultHealthCheckCollectionsJson)
                .fromJsonToList<HealthCheckCollection>()
                .also {
                    _healthCheckCollections = it
                }
        set(value) {
            _healthCheckCollections = value
            prefs.edit { putString(KEY_HEALTH_CHECK_COLLECTIONS, value.toJson()) }
        }

    companion object {
        private const val FILE_NAME = "com.maxciv.healthcheck.preferences.HealthChecksPrefs"

        private const val KEY_HEALTH_CHECK_COLLECTIONS = "KEY_HEALTH_CHECK_COLLECTIONS"
    }
}
