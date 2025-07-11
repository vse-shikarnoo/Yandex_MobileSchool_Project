package yandex.school.project.core.utils

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Функции для форматирования текста в UI.
 * Единственная ответственность: предоставление утилит для работы с текстом в пользовательском интерфейсе.
 */

fun isEmoji(str: String): Boolean {
    val emojiRegex = Regex("[\\u203C-\\u3299\\uD83C-\\uDBFF\\uDC00-\\uDFFF\\uFE0F\\u200D]+")
    return str.matches(emojiRegex)
}

fun getInitials(text: String): String {
    val words = text.trim().split("\\s+".toRegex()).filter { it.isNotEmpty() }
    return when {
        words.isEmpty() -> ""
        words.size == 1 -> words[0].take(2).uppercase()
        else -> (words[0].take(1) + words[1].take(1)).uppercase()
    }
}

fun formatDate(dateString: String): String {
    return try {
        val date = OffsetDateTime.parse(dateString)
        date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))
    } catch (e: Exception) {
        dateString
    }
}

fun formatTime(dateString: String): String {
    return try {
        val date = OffsetDateTime.parse(dateString)
        date.format(DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()))
    } catch (e: Exception) {
        dateString
    }
}