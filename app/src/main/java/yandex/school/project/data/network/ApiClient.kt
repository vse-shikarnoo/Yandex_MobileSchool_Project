package yandex.school.project.data.network

import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiClient {
    companion object {
        private const val BASE_URL = "https://shmr-finance.ru"
        private const val TOKEN = "2NrVL2sbxdmisqnLPGh7NUCe"
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
            header(HttpHeaders.Authorization, "Bearer $TOKEN")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }
} 