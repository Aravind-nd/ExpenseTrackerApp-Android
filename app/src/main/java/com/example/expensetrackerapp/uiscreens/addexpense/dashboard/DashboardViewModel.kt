package com.example.expensetrackerapp.uiscreens.addexpense.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapp.DailyTotal
import com.example.expensetrackerapp.Expense
import com.example.expensetrackerapp.ExpenseDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

data class DashboardUiState(
    val todayTotal: Double = 0.0,
    val monthTotal: Double = 0.0,
    val recentExpenses: List<Expense> = emptyList()
)

class DashboardViewModel(private val dao: ExpenseDao) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        val calendar = Calendar.getInstance()
        val currentMonth = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val currentYear = calendar.get(Calendar.YEAR).toString()

        viewModelScope.launch {
            // Collect daily totals
            dao.getDailyTotals(currentMonth, currentYear).collect { dailyTotals ->
                val todayTotal = dailyTotals.lastOrNull()?.total ?: 0.0
                val monthTotal = dailyTotals.sumOf { it.total }
                
                _uiState.value = _uiState.value.copy(
                    todayTotal = todayTotal,
                    monthTotal = monthTotal
                )
            }
        }

        viewModelScope.launch {
            // Collect recent expenses
            dao.getAllExpenses().collect { expenses ->
                _uiState.value = _uiState.value.copy(
                    recentExpenses = expenses.take(5)
                )
            }
        }
    }
}
