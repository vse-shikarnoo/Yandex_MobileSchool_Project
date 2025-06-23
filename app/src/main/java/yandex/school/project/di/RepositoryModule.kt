package yandex.school.project.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yandex.school.project.data.repository.AccountRepositoryImpl
import yandex.school.project.data.repository.CategoryRepositoryImpl
import yandex.school.project.data.repository.TransactionRepositoryImpl
import yandex.school.project.domain.repositories.AccountRepository
import yandex.school.project.domain.repositories.CategoryRepository
import yandex.school.project.domain.repositories.TransactionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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