package yandex.school.project.di

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        yandex.school.project.core.di.NetworkModule::class,
        yandex.school.project.core.di.RepositoryModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    // Методы для внедрения зависимостей в Application, Activity, ViewModelFactory и т.д.
    fun viewModelFactory(): ViewModelProvider.Factory
    fun accountComponent(): yandex.school.project.account.di.AccountComponent.Factory
    fun categoryComponent(): yandex.school.project.category.di.CategoryComponent.Factory
} 