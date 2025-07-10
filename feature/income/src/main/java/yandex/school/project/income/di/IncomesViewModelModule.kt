package yandex.school.project.income.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import yandex.school.project.core.di.ViewModelKey
import yandex.school.project.income.IncomesViewModel
import yandex.school.project.income.history.IncomesHistoryViewModel

@Module
abstract class IncomesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(IncomesViewModel::class)
    abstract fun bindExpensesViewModel(viewModel: IncomesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(IncomesHistoryViewModel::class)
    abstract fun bindExpensesHistoryViewModel(viewModel: IncomesHistoryViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: yandex.school.project.core.di.ViewModelFactory): ViewModelProvider.Factory

} 