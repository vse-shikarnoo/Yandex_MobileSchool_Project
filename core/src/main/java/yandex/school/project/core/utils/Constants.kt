package yandex.school.project.core.utils

/**
 * Константы, используемые в presentation слое приложения.
 * Единственная ответственность: хранение констант для UI и presentation логики.
 */

const val CURRENCY_RUB = "₽"
const val CURRENCY_USD = "$"
const val CURRENCY_EUR = "€"
const val BALANCE_ZERO = "0 $CURRENCY_RUB"
const val DEFAULT_OPERATION_NAME = "операция"

val currencyRates = mapOf(
    CURRENCY_RUB to 1.0,
    CURRENCY_USD to 1.0 / 70.0,
    CURRENCY_EUR to 1.0 / 90.0
)

fun convertAmount(amount: Double, from: String, to: String): Double {
    val rubAmount = when (from) {
        CURRENCY_RUB -> amount
        CURRENCY_USD -> amount * 70.0
        CURRENCY_EUR -> amount * 90.0
        else -> amount
    }
    return when (to) {
        CURRENCY_RUB -> rubAmount
        CURRENCY_USD -> rubAmount / 70.0
        CURRENCY_EUR -> rubAmount / 90.0
        else -> rubAmount
    }
}
