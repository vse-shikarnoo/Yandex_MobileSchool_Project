package yandex.school.project.account.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import yandex.school.project.account.AccountViewModel
import yandex.school.project.core.di.ViewModelKey

@Module
abstract class AccountViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(viewModel: AccountViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: yandex.school.project.core.di.ViewModelFactory): ViewModelProvider.Factory

} 