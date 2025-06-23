package yandex.school.project.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import yandex.school.project.data.network.ApiClient
import yandex.school.project.data.network.ApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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
} 