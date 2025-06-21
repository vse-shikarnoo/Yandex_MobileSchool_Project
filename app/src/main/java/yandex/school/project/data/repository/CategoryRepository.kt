package yandex.school.project.data.repository

import yandex.school.project.data.models.Category
import yandex.school.project.data.network.ApiService

class CategoryRepository(private val apiService: ApiService) {
    suspend fun getCategories(): List<Category> {
        return apiService.getCategories()
    }
} 