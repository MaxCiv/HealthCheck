package com.maxciv.healthcheck.domain

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * @author maxim.oleynik
 * @since 16.08.2021
 */
@Keep
@Parcelize
@JsonClass(generateAdapter = true)
data class HealthCheckData
constructor(

    @Json(name = "id")
    val id: Long,

    @Json(name = "name")
    val name: String,

    @Json(name = "url")
    val url: String,
) : Parcelable
