package yandex.school.project.core.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import yandex.school.project.core.data.network.ApiClient
import yandex.school.project.core.data.network.ApiService
import yandex.school.project.core.utils.NetworkOperationHelper

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