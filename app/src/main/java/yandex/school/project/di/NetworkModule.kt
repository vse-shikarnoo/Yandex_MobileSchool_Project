package yandex.school.project.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import yandex.school.project.data.network.ApiClient
import yandex.school.project.data.network.ApiService
import yandex.school.project.presentation.common.NetworkOperationHelper

/**
 * Модуль Dagger для предоставления сетевых зависимостей.
 * Единственная ответственность: настройка и предоставление зависимостей для сетевого слоя приложения.
 */
@Module
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideApiClient(): ApiClient {
        return ApiClient()
    }
    
    @Provides
    @Singleton
    fun provideApiService(apiClient: ApiClient): ApiService {
        return ApiService(apiClient)
    }

    @Provides
    fun provideNetworkOperationHelper(): NetworkOperationHelper = NetworkOperationHelper()
} 