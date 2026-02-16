package com.example.expensetrackerapp.uiscreens.addexpense



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapp.Expense
import com.example.expensetrackerapp.ExpenseDao
import kotlinx.coroutines.launch

class AddExpenseViewModel(private val dao: ExpenseDao) : ViewModel() {

    // UI state
    var amount by mutableStateOf("")
    var category by mutableStateOf("Select Category")
    var date by mutableStateOf(System.currentTimeMillis())
    var paymentMethod by mutableStateOf("Cash")
    var note by mutableStateOf("")
    var showSuccess by mutableStateOf(false)

    // Categories & Payment Methods
    val categories = listOf("Select Category", "Food", "Transport", "Shopping", "Bills", "Entertainment", "Other")
    val paymentMethods = listOf("Cash", "Card", "UPI")

    // Category Icons
    fun iconForCategory(cat: String): String = when (cat) {
        "Food" -> "🍴"
        "Transport" -> "🚗"
        "Shopping" -> "🛍️"
        "Bills" -> "💡"
        "Entertainment" -> "🎬"
        "Other" -> "❓"
        else -> "💳"
    }

    fun addExpense() {
        val amt = amount.toDoubleOrNull() ?: return
        if (amt <= 0 || category == "Select Category") return

        val expense = Expense(
            amount = amt,
            category = category,
            date = date,
            paymentMethod = paymentMethod,
            note = note
        )

        viewModelScope.launch {
            try {
                dao.insertExpense(expense)
                showSuccess = true
                resetFields()
            } catch (e: Exception) {
                e.printStackTrace()
                // Log the error
            }
        }
    }

    private fun resetFields() {
        amount = ""
        category = "Select Category"
        date = System.currentTimeMillis()
        paymentMethod = "Cash"
        note = ""
    }
}
