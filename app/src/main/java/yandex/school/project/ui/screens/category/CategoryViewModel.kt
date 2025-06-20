package yandex.school.project.ui.screens.category

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import yandex.school.project.data.models.Category
import yandex.school.project.data.repository.CategoryRepository
import yandex.school.project.data.network.ApiService
import yandex.school.project.data.network.ApiClient
import yandex.school.project.ui.common.Result
import java.io.IOException
import java.net.UnknownHostException

class CategoryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<Result<List<Category>>>(Result.Loading)
    val uiState: StateFlow<Result<List<Category>>> = _uiState.asStateFlow()

    private val repository = CategoryRepository(ApiService(ApiClient()))

    init {
        loadCategoriesWithRetry()
    }

    fun loadCategoriesWithRetry(maxRetries: Int = 3, delayMillis: Long = 2000) {
        viewModelScope.launch {
            _uiState.value = Result.Loading
            var attempt = 0
            var success = false
            var lastError: Exception? = null
            while (attempt < maxRetries && !success) {
                try {
                    val result = repository.getCategories()
                    _uiState.value = Result.Success(result)
                    success = true
                } catch (e: Exception) {
                    lastError = e
                    attempt++
                    if (e is UnknownHostException || e is IOException) {
                        _uiState.value = Result.Error("Нет подключения к интернету")
                        break
                    }
                    if (attempt < maxRetries) {
                        delay(delayMillis)
                    }
                }finally {
                    Log.d("RetryTest", "loadCategoriesWithRetry: $attempt")
                }
            }
            if (!success && lastError != null) {
                _uiState.value = Result.Error(lastError.message ?: "Неизвестная ошибка")
            }
        }
    }
}