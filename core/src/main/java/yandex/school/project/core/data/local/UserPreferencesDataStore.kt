package yandex.school.project.core.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object UserPreferencesKeys {
    val PRIMARY_COLOR = longPreferencesKey("primary_color")
    val SECONDARY_COLOR = longPreferencesKey("secondary_color")
    val DARK_THEME = booleanPreferencesKey("dark_theme")
    val HAPTICS_ENABLED = booleanPreferencesKey("haptics_enabled")
}

val Context.userPreferencesDataStore by preferencesDataStore(name = "user_preferences")

class UserPreferencesDataStore(private val context: Context) {
    val preferencesFlow: Flow<UserPreferences> = context.userPreferencesDataStore.data.map { preferences ->
        UserPreferences(
            primaryColor = (preferences[UserPreferencesKeys.PRIMARY_COLOR]?.toULong() ?: 0xFF2AE881UL) and 0xFFFFFFFFUL,
            secondaryColor = (preferences[UserPreferencesKeys.SECONDARY_COLOR]?.toULong() ?: 0xFFD4FAE6UL) and 0xFFFFFFFFUL,
            darkTheme = preferences[UserPreferencesKeys.DARK_THEME] ?: false,
            hapticsEnabled = preferences[UserPreferencesKeys.HAPTICS_ENABLED] ?: false
        )
    }

    suspend fun updateColors(primary: ULong, secondary: ULong) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.PRIMARY_COLOR] = primary.toLong()
            preferences[UserPreferencesKeys.SECONDARY_COLOR] = secondary.toLong()
        }
    }

    suspend fun updateDarkTheme(enabled: Boolean) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.DARK_THEME] = enabled
        }
    }

    suspend fun updateHapticsEnabled(enabled: Boolean) {
        context.userPreferencesDataStore.edit { preferences ->
            preferences[UserPreferencesKeys.HAPTICS_ENABLED] = enabled
        }
    }
}

data class UserPreferences(
    val primaryColor: ULong,
    val secondaryColor: ULong,
    val darkTheme: Boolean,
    val hapticsEnabled: Boolean
) 