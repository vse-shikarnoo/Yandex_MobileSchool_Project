package yandex.school.project.income.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import yandex.school.project.core.domain.entities.Category
import yandex.school.project.core.domain.entities.TransactionType
import yandex.school.project.core.domain.entities.TransactionWithCategory
import yandex.school.project.core.domain.usecases.category.GetCategoriesUseCase
import yandex.school.project.core.domain.usecases.transaction.GetTransactionsByAccountUseCase
import yandex.school.project.core.utils.Result
import java.time.LocalDate
import java.time.OffsetDateTime
import javax.inject.Inject

data class CategoryAnalysis(
    val category: Category,
    val percent: Int,
    val amount: Double,
    val transactions: List<TransactionWithCategory>
)

data class IncomesAnalysisState(
    val startDate: LocalDate = LocalDate.now().withDayOfMonth(1),
    val endDate: LocalDate = LocalDate.now(),
    val total: Double = 0.0,
    val categories: List<CategoryAnalysis> = emptyList()
)

class IncomesAnalysisViewModel @Inject constructor(
    private val getTransactionsByAccountUseCase: GetTransactionsByAccountUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<Result<IncomesAnalysisState>>(Result.Loading)
    val uiState: StateFlow<Result<IncomesAnalysisState>> = _uiState

    private var accountId: Int = -1

    fun setAccountId(id: Int) {
        accountId = id
        loadAnalysis()
    }

    fun setPeriod(start: LocalDate, end: LocalDate) {
        _uiState.update {
            val current = (it as? Result.Success)?.data ?: IncomesAnalysisState()
            Result.Success(current.copy(startDate = start, endDate = end))
        }
        loadAnalysis()
    }

    private fun loadAnalysis() {
        val state = (uiState.value as? Result.Success)?.data ?: IncomesAnalysisState()
        viewModelScope.launch {
            getTransactionsByAccountUseCase(accountId)
                .combine(getCategoriesUseCase()) { transactions, categories ->
                    val filtered = transactions.filter { t ->
                        t.type == TransactionType.EXPENSE &&
                        isInPeriod(t.date, state.startDate, state.endDate)
                    }
                    val withCategory = filtered.mapNotNull { t ->
                        val cat = categories.find { it.id == t.categoryId }
                        cat?.let { TransactionWithCategory(t, it) }
                    }
                    val total = withCategory.sumOf { it.amount }
                    val grouped = withCategory.groupBy { it.category }
                    val categoryAnalysis = grouped.map { (cat, list) ->
                        val sum = list.sumOf { it.amount }
                        val percent = if (total > 0) (sum / total * 100).toInt() else 0
                        CategoryAnalysis(cat, percent, sum, list)
                    }.sortedByDescending { it.amount }
                    IncomesAnalysisState(
                        startDate = state.startDate,
                        endDate = state.endDate,
                        total = total,
                        categories = categoryAnalysis
                    )
                }
                .catch { e -> _uiState.value = Result.Error(e.message ?: "Ошибка анализа") }
                .collect { s -> _uiState.value = Result.Success(s) }
        }
    }

    private fun isInPeriod(dateStr: String, start: LocalDate, end: LocalDate): Boolean {
        return try {
            val date = OffsetDateTime.parse(dateStr).toLocalDate()
            !date.isBefore(start) && !date.isAfter(end)
        } catch (e: Exception) {
            true
        }
    }
} 