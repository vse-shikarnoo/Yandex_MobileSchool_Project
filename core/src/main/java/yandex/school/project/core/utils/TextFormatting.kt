package yandex.school.project.core.utils

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