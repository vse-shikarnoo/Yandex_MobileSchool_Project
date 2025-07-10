package yandex.school.project.di

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        yandex.school.project.core.di.NetworkModule::class,
        yandex.school.project.core.di.RepositoryModule::class
    ]
)
interface AppComponent {
    // Методы для внедрения зависимостей в Application, Activity, ViewModelFactory и т.д.
    fun accountComponent(): yandex.school.project.account.di.AccountComponent.Factory
    fun categoryComponent(): yandex.school.project.category.di.CategoryComponent.Factory
    fun incomesComponent(): yandex.school.project.income.di.IncomesComponent.Factory
    fun expensesComponent(): yandex.school.project.expenses.di.ExpensesComponent.Factory
    fun splashComponent(): yandex.school.project.splash.di.SplashComponent.Factory
} 