package yandex.school.project.category.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import yandex.school.project.category.CategoryViewModel
import yandex.school.project.core.di.ViewModelKey

@Module
abstract class CategoryViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(viewModel: CategoryViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: yandex.school.project.core.di.ViewModelFactory): ViewModelProvider.Factory

} 