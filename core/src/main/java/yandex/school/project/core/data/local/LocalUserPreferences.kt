package yandex.school.project.core.data.local

import androidx.compose.runtime.compositionLocalOf

val LocalUserPreferences = compositionLocalOf<UserPreferences> { error("No UserPreferences provided") } 