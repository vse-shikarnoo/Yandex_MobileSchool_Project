package yandex.school.project.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import yandex.school.project.core.di.ViewModelFactory
import yandex.school.project.core.di.ViewModelKey
import yandex.school.project.presentation.screens.expenses.expenses.ExpensesViewModel
import yandex.school.project.presentation.screens.income.incomes.IncomesViewModel
import yandex.school.project.presentation.screens.splash.SplashViewModel
import yandex.school.project.presentation.screens.expenses.history.ExpensesHistoryViewModel
import yandex.school.project.presentation.screens.income.history.IncomesHistoryViewModel

@Module
abstract class ViewModelModule {



    @Binds
    @IntoMap
    @ViewModelKey(IncomesViewModel::class)
    abstract fun bindIncomesViewModel(viewModel: IncomesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(viewModel: SplashViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(IncomesHistoryViewModel::class)
    abstract fun bindIncomesHistoryViewModel(viewModel: IncomesHistoryViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
} 