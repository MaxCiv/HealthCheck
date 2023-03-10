package android.content

/**
 * @author maxim.oleynik
 * @since 18.08.2021
 */
fun SharedPreferences.getStringSafe(key: String, defaultValue: String): String {
    return getString(key, defaultValue)!!
}
