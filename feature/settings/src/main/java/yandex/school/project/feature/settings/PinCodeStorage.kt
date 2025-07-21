package yandex.school.project.feature.settings

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object PinCodeStorage {
    private const val PREF_NAME = "pin_prefs"
    private const val KEY_PIN = "pin_code"

    private fun getPrefs(context: Context) = EncryptedSharedPreferences.create(
        context,
        PREF_NAME,
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun savePin(context: Context, pin: String) {
        getPrefs(context).edit { putString(KEY_PIN, pin) }
    }

    fun getPin(context: Context): String? = getPrefs(context).getString(KEY_PIN, null)

    fun hasPin(context: Context): Boolean = getPin(context) != null

    fun checkPin(context: Context, pin: String): Boolean = getPin(context) == pin

    fun clearPin(context: Context) {
        getPrefs(context).edit { remove(KEY_PIN) }
    }
} 