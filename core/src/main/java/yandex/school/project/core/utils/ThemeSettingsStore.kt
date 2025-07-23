package yandex.school.project.core.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ThemeSettingsStore {
    private const val DATASTORE_NAME = "theme_settings"
    private val Context.dataStore by preferencesDataStore(DATASTORE_NAME)

    private val KEY_DARK_THEME = booleanPreferencesKey("dark_theme")
    private val KEY_PRIMARY_COLOR = longPreferencesKey("primary_color")
    private val KEY_SECONDARY_COLOR = longPreferencesKey("secondary_color")
    private val KEY_HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")

    data class ThemeSettings(
        val darkTheme: Boolean = false,
        val primaryColor: Long = 0xFF2AE881,
        val secondaryColor: Long = 0xFFD4FAE6,
        val hapticsEnabled: Boolean = false
    )

    fun themeSettingsFlow(context: Context): Flow<ThemeSettings> =
        context.dataStore.data.map { prefs ->
            ThemeSettings(
                darkTheme = prefs[KEY_DARK_THEME] ?: false,
                primaryColor = prefs[KEY_PRIMARY_COLOR]?.takeIf { it != 0L } ?: 0xFF2AE881,
                secondaryColor = prefs[KEY_SECONDARY_COLOR]?.takeIf { it != 0L } ?: 0xFFD4FAE6,
                hapticsEnabled = prefs[KEY_HAPTICS_ENABLED] ?: true
            )
        }

    suspend fun setDarkTheme(context: Context, value: Boolean) {
        context.dataStore.edit { it[KEY_DARK_THEME] = value }
    }
    suspend fun setPrimaryColor(context: Context, value: Long) {
        context.dataStore.edit { it[KEY_PRIMARY_COLOR] = value }
    }
    suspend fun setSecondaryColor(context: Context, value: Long) {
        if (value == 0L) {
            Log.d("ThemeSettingsStore", "Не сохраняю secondaryColor, так как value == 0")
            return
        }
        Log.d("ThemeSettingsStore", "Запись secondaryColor в DataStore: $value")
        context.dataStore.edit { it[KEY_SECONDARY_COLOR] = value }
    }
    suspend fun setHapticsEnabled(context: Context, value: Boolean) {
        context.dataStore.edit { it[KEY_HAPTICS_ENABLED] = value }
    }
} 