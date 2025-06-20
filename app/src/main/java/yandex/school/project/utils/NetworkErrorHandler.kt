package yandex.school.project.utils

import android.util.Log
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * Утилита для обработки сетевых ошибок
 */
object NetworkErrorHandler {
    
    private const val TAG = "NetworkErrorHandler"
    
    /**
     * Обрабатывает сетевую ошибку и возвращает понятное сообщение для пользователя
     */
    fun handleNetworkError(exception: Exception, operation: String = "операция"): String {
        Log.e(TAG, "Ошибка при выполнении $operation: ${exception.javaClass.simpleName} - ${exception.message}")
        Log.e(TAG, "Полный стек ошибки:", exception)
        
        return when (exception) {
            is UnknownHostException -> {
                Log.d(TAG, "Обнаружена ошибка UnknownHostException")
                "Не удается найти сервер. Проверьте подключение к интернету."
            }
            is ConnectException -> {
                Log.d(TAG, "Обнаружена ошибка ConnectException")
                "Не удается подключиться к серверу. Сервер может быть недоступен."
            }
            is SocketException -> {
                Log.d(TAG, "Обнаружена ошибка SocketException")
                "Ошибка сетевого сокета. Возможно, проблема с правами доступа к сети."
            }
            is SocketTimeoutException -> {
                Log.d(TAG, "Обнаружена ошибка SocketTimeoutException")
                "Превышено время ожидания ответа от сервера."
            }
            is SSLException -> {
                Log.d(TAG, "Обнаружена ошибка SSLException")
                "Ошибка SSL соединения. Проблема с сертификатом сервера."
            }
            is IOException -> {
                Log.d(TAG, "Обнаружена ошибка IOException")
                "Ошибка сетевого соединения. Проверьте подключение к интернету."
            }
            else -> {
                Log.d(TAG, "Неизвестная ошибка: ${exception.javaClass.simpleName}")
                exception.message ?: "Неизвестная ошибка"
            }
        }
    }
    
    /**
     * Проверяет, является ли ошибка сетевой (требует повторной попытки)
     */
    fun isNetworkError(exception: Exception): Boolean {
        return when (exception) {
            is UnknownHostException,
            is ConnectException,
            is SocketException,
            is SocketTimeoutException,
            is SSLException,
            is IOException -> true
            else -> false
        }
    }
    
    /**
     * Проверяет, является ли ошибка критической (не требует повторной попытки)
     */
    fun isCriticalError(exception: Exception): Boolean {
        return when (exception) {
            is UnknownHostException,
            is ConnectException,
            is SocketException,
            is SSLException -> true
            else -> false
        }
    }
    
    /**
     * Получает тип ошибки для логирования
     */
    fun getErrorType(exception: Exception): String {
        return when (exception) {
            is UnknownHostException -> "UnknownHostException"
            is ConnectException -> "ConnectException"
            is SocketException -> "SocketException"
            is SocketTimeoutException -> "SocketTimeoutException"
            is SSLException -> "SSLException"
            is IOException -> "IOException"
            else -> exception.javaClass.simpleName
        }
    }
} 