package yandex.school.project.expenses.di

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModelProvider
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

val LocalExpensesViewModelFactory = staticCompositionLocalOf<ViewModelProvider.Factory> {
    error("ExpensesViewModelFactory not provided")
}

@Subcomponent(
//@Singleton
//@Component(
    modules = [ExpensesViewModelModule::class]
)
interface ExpensesComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): ExpensesComponent
    }
} 