package yandex.school.project.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryProvideModule {
    // Здесь больше нет provideAccountRepository
} 