package yandex.school.project.category.di

import androidx.compose.runtime.staticCompositionLocalOf
import dagger.Subcomponent
import androidx.lifecycle.ViewModelProvider

val LocalCategoryViewModelFactory = staticCompositionLocalOf<ViewModelProvider.Factory> {
    error("CategoryViewModelFactory not provided")
}

@Subcomponent(modules = [CategoryViewModelModule::class])
interface CategoryComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Subcomponent.Factory
    interface Factory {
        fun create(): CategoryComponent
    }
} 