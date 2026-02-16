package com.example.expensetrackerapp.uiscreens.addexpense.expenselists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapp.Expense
import com.example.expensetrackerapp.ExpenseDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpenseDetailViewModel(
    private val expenseDao: ExpenseDao,
    expenseId: Int
) : ViewModel() {

    private val _expense = MutableStateFlow<Expense?>(null)
    val expense: StateFlow<Expense?> = _expense

    init {
        viewModelScope.launch {
            expenseDao.getAllExpenses().collect { list ->
                _expense.value = list.find { it.id == expenseId }
            }
        }
    }
}
