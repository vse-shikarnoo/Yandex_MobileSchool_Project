package yandex.school.project.core.data.network

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * HTTP клиент для работы с API, отвечающий за настройку сетевых запросов.
 * Единственная ответственность: конфигурация и предоставление настроенного HTTP клиента для API запросов.
 */
class ApiClient {
    companion object {
        private const val BASE_URL = "https://shmr-finance.ru"

        object BuildConfig{
            val API_TOKEN = "kasljdklsadjlakjdklasjdkl"
        }
    }

    init {
        Log.d("ApiClient", "Инициализация ApiClient с BASE_URL: $BASE_URL")
        Log.d("ApiClient", "Токен: ${BuildConfig.API_TOKEN.take(10)}...")
    }

    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
                encodeDefaults = true
            })
        }

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        defaultRequest {
            url(BASE_URL)
            header(HttpHeaders.Authorization, "Bearer ${BuildConfig.API_TOKEN}")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            Log.d("ApiClient", "Настройка defaultRequest: ${url.buildString()}")
        }
    }
} 