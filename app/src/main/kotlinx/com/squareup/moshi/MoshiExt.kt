package com.squareup.moshi

/**
 * @author maxim.oleynik
 * @since 16.08.2021
 */
inline fun <T : Map<String, V>, reified V> T.toJsonFromMap(moshi: Moshi = Moshi.Builder().build()): String {
    val type = Types.newParameterizedType(Map::class.java, String::class.java, V::class.java)
    val adapter: JsonAdapter<Map<String, V>> = moshi.adapter(type)
    return adapter.toJson(this)
}

fun <T> T.toJson(moshi: Moshi = Moshi.Builder().build()): String {
    val adapter = moshi.adapter<Any>(Object::class.java)
    return adapter.toJson(this)
}

inline fun <reified V> String.fromJsonToMap(moshi: Moshi = Moshi.Builder().build()): Map<String, V> {
    val type = Types.newParameterizedType(Map::class.java, String::class.java, V::class.java)
    val adapter: JsonAdapter<Map<String, V>> = moshi.adapter(type)
    return adapter.fromJson(this) ?: error("Moshi can't parse: '$this'")
}

inline fun <reified T> String.fromJsonToList(moshi: Moshi = Moshi.Builder().build()): List<T> {
    val type = Types.newParameterizedType(List::class.java, T::class.java)
    val adapter: JsonAdapter<List<T>> = moshi.adapter(type)
    return adapter.fromJson(this) ?: error("Moshi can't parse: '$this'")
}

inline fun <reified T> String.fromJson(moshi: Moshi = Moshi.Builder().build()): T {
    val adapter: JsonAdapter<T> = moshi.adapter(T::class.java)
    return adapter.fromJson(this) ?: error("Moshi can't parse: '$this'")
}
