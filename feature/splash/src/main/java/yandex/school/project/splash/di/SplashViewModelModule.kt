package yandex.school.project.splash.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import yandex.school.project.core.di.ViewModelKey
import yandex.school.project.splash.SplashViewModel

@Module
abstract class SplashViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindAccountViewModel(viewModel: SplashViewModel): ViewModel
} 