package yandex.school.project.splash.di

import androidx.compose.runtime.staticCompositionLocalOf
import dagger.Subcomponent
import androidx.lifecycle.ViewModelProvider

val LocalSplashViewModelFactory = staticCompositionLocalOf<ViewModelProvider.Factory> {
    error("SplashViewModelFactory not provided")
}

@Subcomponent(modules = [SplashViewModelModule::class])
interface SplashComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): SplashComponent
    }
} 