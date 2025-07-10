package yandex.school.project.expenses.di

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModelProvider
import dagger.Component
import javax.inject.Singleton

val LocalExpensesViewModelFactory = staticCompositionLocalOf<ViewModelProvider.Factory> {
    error("ExpensesViewModelFactory not provided")
}

//@Subcomponent(modules = [ExpensesViewModelModule::class])
@Singleton
@Component(
    modules = [ExpensesViewModelModule::class,
        yandex.school.project.core.di.NetworkModule::class,
        yandex.school.project.core.di.RepositoryModule::class]
)
interface ExpensesComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(): ExpensesComponent
    }
} 