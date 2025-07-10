package yandex.school.project.account.di

import androidx.compose.runtime.staticCompositionLocalOf
import dagger.Subcomponent
import androidx.lifecycle.ViewModelProvider

val LocalAccountViewModelFactory = staticCompositionLocalOf<ViewModelProvider.Factory> {
    error("AccountViewModelFactory not provided")
}

@Subcomponent(modules = [AccountViewModelModule::class])
interface AccountComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): AccountComponent
    }
} 