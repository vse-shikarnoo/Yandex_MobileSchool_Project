package yandex.school.project.core.di

import dagger.Binds
import dagger.Module
import yandex.school.project.core.data.repository.AccountRepositoryImpl
import yandex.school.project.core.data.repository.CategoryRepositoryImpl
import yandex.school.project.core.data.repository.TransactionRepositoryImpl
import yandex.school.project.core.domain.repositories.AccountRepository
import yandex.school.project.core.domain.repositories.CategoryRepository
import yandex.school.project.core.domain.repositories.TransactionRepository
import javax.inject.Singleton

/**
 * Модуль Dagger для привязки репозиториев к их интерфейсам.
 * Единственная ответственность: настройка dependency injection для репозиториев.
 */
@Module
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        accountRepositoryImpl: AccountRepositoryImpl
    ): AccountRepository
    
    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository
    
    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository
} 