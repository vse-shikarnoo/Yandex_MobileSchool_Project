package yandex.school.project.account.di

import dagger.Subcomponent
import androidx.lifecycle.ViewModelProvider

@Subcomponent(modules = [AccountViewModelModule::class])
interface AccountComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): AccountComponent
    }
} 