package yandex.school.project.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import yandex.school.project.core.domain.usecases.account.GetAccountByIdUseCase
import yandex.school.project.core.domain.usecases.account.UpdateAccountNameUseCase
import yandex.school.project.core.domain.usecases.account.UpdateAccountBalanceUseCase
import yandex.school.project.core.domain.usecases.account.UpdateAccountCurrencyUseCase
import yandex.school.project.core.domain.usecases.account.GetAccountHistoryUseCase
import yandex.school.project.charts.BarChartData
import yandex.school.project.core.utils.NetworkOperationHelper
import javax.inject.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

/**
 * ViewModel для экрана аккаунта, управляющий состоянием и загрузкой данных аккаунта.
 * Единственная ответственность: управление состоянием UI и загрузка данных конкретного аккаунта.
 */
class AccountViewModel @Inject constructor(
    private val getAccountByIdUseCase: GetAccountByIdUseCase,
    private val updateAccountNameUseCase: UpdateAccountNameUseCase,
    private val updateAccountBalanceUseCase: UpdateAccountBalanceUseCase,
    private val updateAccountCurrencyUseCase: UpdateAccountCurrencyUseCase,
    private val getAccountHistoryUseCase: GetAccountHistoryUseCase,
    private val networkHelper: NetworkOperationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow<yandex.school.project.core.utils.Result<yandex.school.project.core.domain.entities.Account>>(
        yandex.school.project.core.utils.Result.Loading)
    val uiState: StateFlow<yandex.school.project.core.utils.Result<yandex.school.project.core.domain.entities.Account>> = _uiState

    private val _historyBarChart = MutableStateFlow<List<BarChartData>>(emptyList())
    val historyBarChart: StateFlow<List<BarChartData>> = _historyBarChart.asStateFlow()

    override fun onCleared() {
        super.onCleared()
        Log.d("${this::class.java}", "onCleared: ")
    }

    fun loadAccount(accountId: Int) {
        networkHelper.executeOnce(
            scope = viewModelScope,
            operation = { getAccountByIdUseCase(accountId) },
            onSuccess = { account ->
                _uiState.value = yandex.school.project.core.utils.Result.Success(account)
            },
            onError = { errorMessage ->
                _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage)
            },
            operationName = "загрузка аккаунта"
        )
    }

    fun loadAccountWithRetry(accountId: Int, maxRetries: Int = 3, delayMillis: Long = 2000) {
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = { getAccountByIdUseCase(accountId) },
            onSuccess = { account ->
                _uiState.value = yandex.school.project.core.utils.Result.Success(account)
            },
            onError = { errorMessage ->
                _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage)
            },
            maxRetries = maxRetries,
            delayMillis = delayMillis,
            operationName = "загрузка аккаунта"
        )
    }

    fun updateAccountName(newName: String) {
        val account = (uiState.value as? yandex.school.project.core.utils.Result.Success)?.data ?: return
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = { updateAccountNameUseCase(account.id, newName, account.balance, account.currency) },
            onSuccess = { acc -> _uiState.value = yandex.school.project.core.utils.Result.Success(acc) },
            onError = { errorMessage -> _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage) },
            operationName = "обновление имени аккаунта ${account.id}, ${newName}, ${account.balance}, ${account.currency}"
        )
    }

    fun updateAccountBalance(newBalance: Double) {
        val account = (uiState.value as? yandex.school.project.core.utils.Result.Success)?.data ?: return

        // Оптимистичное обновление UI
        _uiState.value = yandex.school.project.core.utils.Result.Success(account.copy(balance = newBalance))

        // Асинхронное обновление на сервере
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = { updateAccountBalanceUseCase(account.id, account.name, newBalance, account.currency) },
            onSuccess = { acc -> _uiState.value = yandex.school.project.core.utils.Result.Success(acc) },
            onError = { errorMessage -> _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage) },
            operationName = "обновление баланса аккаунта ${account.id}, ${account.name}, $newBalance, ${account.currency}"
        )
    }

    fun updateAccountCurrency(newCurrency: String) {
        val account = (uiState.value as? yandex.school.project.core.utils.Result.Success)?.data ?: return
        networkHelper.executeWithRetry(
            scope = viewModelScope,
            operation = { updateAccountCurrencyUseCase(account.id, account.name, account.balance, newCurrency) },
            onSuccess = { acc -> _uiState.value = yandex.school.project.core.utils.Result.Success(acc) },
            onError = { errorMessage -> _uiState.value = yandex.school.project.core.utils.Result.Error(errorMessage) },
            operationName = "обновление валюты аккаунта ${account.id}, ${account.name}, ${account.balance}, ${newCurrency}"
        )
    }

    fun loadAccountHistory(accountId: Int) {
        networkHelper.executeOnce(
            scope = viewModelScope,
            operation = { getAccountHistoryUseCase(accountId) },
            onSuccess = { history ->
                val formatter = DateTimeFormatter.ISO_LOCAL_DATE
                val now = LocalDate.now()
                val firstDay = now.withDayOfMonth(1)
                val lastDay = now.with(TemporalAdjusters.lastDayOfMonth())
                val daysInMonth = lastDay.dayOfMonth
                // Группируем по дате (день)
                val grouped = history.transactions
                    .filter { t ->
                        val date = LocalDate.parse(t.date.substring(0, 10), formatter)
                        !date.isBefore(firstDay) && !date.isAfter(lastDay)
                    }
                    .groupBy { t -> t.date.substring(0, 10) }
                // Для каждого дня месяца — сумма изменений
                val chartData = (1..daysInMonth).map { day ->
                    val date = firstDay.withDayOfMonth(day)
                    val label = when (day) {
                        1 -> date.format(DateTimeFormatter.ofPattern("dd.MM"))
                        daysInMonth -> date.format(DateTimeFormatter.ofPattern("dd.MM"))
                        daysInMonth/2 -> date.format(DateTimeFormatter.ofPattern("dd.MM"))
                        else -> ""
                    }
                    val sum = grouped[date.toString()]?.sumOf { it.amount } ?: 0.0
                    BarChartData(sum.toFloat(), label)
                }
                _historyBarChart.value = chartData
            },
            onError = { _historyBarChart.value = emptyList() },
            operationName = "загрузка истории аккаунта"
        )
    }
} 