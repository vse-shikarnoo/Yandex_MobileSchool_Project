package yandex.school.project.data.network

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
import yandex.school.project.BuildConfig

class ApiClient {
    companion object {
        private const val BASE_URL = "https://shmr-finance.ru"
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