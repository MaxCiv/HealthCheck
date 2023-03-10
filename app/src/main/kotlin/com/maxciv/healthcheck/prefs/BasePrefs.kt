package com.maxciv.healthcheck.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * @author maxim.oleynik
 * @since 16.08.2021
 */
abstract class BasePrefs
constructor(
        context: Context,
        fileName: String,
) {

    protected val prefs: SharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE)

    open fun clear() {
        prefs.edit(commit = true) {
            clear()
        }
    }
}
