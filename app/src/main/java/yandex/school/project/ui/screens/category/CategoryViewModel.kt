package yandex.school.project.ui.screens.category

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.SocketException
import javax.net.ssl.SSLException
import yandex.school.project.ui.common.BaseNetworkViewModel

class CategoryViewModel : BaseNetworkViewModel() {
    private val _uiState = MutableStateFlow<Result<List<Category>>>(Result.Loading)
    val uiState: StateFlow<Result<List<Category>>> = _uiState.asStateFlow()

    private val repository = CategoryRepository(ApiService(ApiClient()))

    init {
        loadCategoriesWithRetry()
    }

    fun loadCategoriesWithRetry(maxRetries: Int = 3, delayMillis: Long = 2000) {
        executeWithRetry(
            operation = { repository.getCategories() },
            onSuccess = { categories ->
                Log.d("CategoryViewModel", "Категории успешно загружены: ${categories.size} элементов")
                _uiState.value = Result.Success(categories)
            },
            onError = { errorMessage ->
                _uiState.value = Result.Error(errorMessage)
            },
            maxRetries = maxRetries,
            delayMillis = delayMillis,
            operationName = "загрузка категорий"
        )
    }
}