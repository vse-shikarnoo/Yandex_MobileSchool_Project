package yandex.school.project.di

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    // Методы для внедрения зависимостей в Application, Activity, ViewModelFactory и т.д.
    fun viewModelFactory(): ViewModelProvider.Factory
} 