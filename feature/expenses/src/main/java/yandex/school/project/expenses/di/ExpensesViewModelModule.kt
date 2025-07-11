package yandex.school.project.expenses.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import yandex.school.project.core.di.ViewModelKey
import yandex.school.project.expenses.ExpensesViewModel
import yandex.school.project.expenses.edit.ExpensesEditViewModel
import yandex.school.project.expenses.history.ExpensesHistoryViewModel

@Module
abstract class ExpensesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesViewModel::class)
    abstract fun bindExpensesViewModel(viewModel: ExpensesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesHistoryViewModel::class)
    abstract fun bindExpensesHistoryViewModel(viewModel: ExpensesHistoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExpensesEditViewModel::class)
    abstract fun bindExpensesEditViewModel(viewModel: ExpensesEditViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: yandex.school.project.core.di.ViewModelFactory): ViewModelProvider.Factory

} 