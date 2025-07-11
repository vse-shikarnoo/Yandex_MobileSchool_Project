package yandex.school.project.income.di

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModelProvider
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

val LocalIncomesViewModelFactory = staticCompositionLocalOf<ViewModelProvider.Factory> {
    error("IncomesViewModelFactory not provided")
}

@Subcomponent(//modules = [IncomesViewModelModule::class])
//@Singleton
//@Component(
    modules = [IncomesViewModelModule::class]
)
interface IncomesComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): IncomesComponent
    }
} 