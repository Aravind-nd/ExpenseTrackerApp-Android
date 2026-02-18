package com.example.expensetrackerapp.uiscreens.addexpense.analytics

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapp.CategoryTotal
import com.example.expensetrackerapp.ExpenseDao
import com.example.expensetrackerapp.WeeklyTotal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class AnalyticsUiState(
    val categoryTotals: List<CategoryTotal> = emptyList(),
    val weeklyTotals: List<WeeklyTotal> = emptyList(),
    val totalSpending: Double = 0.0,
    val avgSpending: Double = 0.0,
    val topCategory: String = "-"
)

class AnalyticsViewModel(private val dao: ExpenseDao) : ViewModel() {

    private val calendar = Calendar.getInstance()
    
    var selectedMonth by mutableStateOf(calendar.get(Calendar.MONTH))
        private set
    
    var selectedYear by mutableStateOf(calendar.get(Calendar.YEAR))
        private set

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalyticsData()
    }

    fun updateMonth(month: Int) {
        selectedMonth = month
        loadAnalyticsData()
    }

    fun updateYear(year: Int) {
        selectedYear = year
        loadAnalyticsData()
    }

    private fun loadAnalyticsData() {
        val monthStr = String.format("%02d", selectedMonth + 1)
        val yearStr = selectedYear.toString()

        viewModelScope.launch {
            dao.getCategoryTotals(monthStr, yearStr).collect { categories ->
                val topCat = categories.maxByOrNull { it.total }?.category ?: "-"
                
                _uiState.value = _uiState.value.copy(
                    categoryTotals = categories,
                    topCategory = topCat
                )
            }
        }

        viewModelScope.launch {
            dao.getWeeklyTotals(monthStr, yearStr).collect { weekly ->
                val total = weekly.sumOf { it.total }
                val avg = if (weekly.isEmpty()) 0.0 else total / weekly.size

                _uiState.value = _uiState.value.copy(
                    weeklyTotals = weekly,
                    totalSpending = total,
                    avgSpending = avg
                )
            }
        }
    }
}
