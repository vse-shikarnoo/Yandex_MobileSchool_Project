package yandex.school.project.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.BindsInstance
import dagger.Component
import yandex.school.project.account.di.AccountComponent
import yandex.school.project.category.di.CategoryComponent
import yandex.school.project.core.di.NetworkModule
import yandex.school.project.core.di.RepositoryModule
import yandex.school.project.core.di.RoomModule
import yandex.school.project.core.di.SyncTransactionsWorkerFactory
import yandex.school.project.core.di.WorkerModule
import yandex.school.project.expenses.di.ExpensesComponent
import yandex.school.project.income.di.IncomesComponent
import yandex.school.project.splash.di.SplashComponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        RepositoryModule::class,
        RoomModule::class,
        WorkerModule::class
    ]
)
interface AppComponent {
    // Методы для внедрения зависимостей в Application, Activity, ViewModelFactory и т.д.
    fun accountComponent(): AccountComponent.Factory
    fun categoryComponent(): CategoryComponent.Factory
    fun incomesComponent(): IncomesComponent.Factory
    fun expensesComponent(): ExpensesComponent.Factory
    fun splashComponent(): SplashComponent.Factory

    fun syncTransactionsWorkerFactory(): SyncTransactionsWorkerFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }


} 