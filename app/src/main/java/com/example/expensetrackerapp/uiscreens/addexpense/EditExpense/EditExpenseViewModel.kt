import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetrackerapp.Expense
import com.example.expensetrackerapp.ExpenseDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditExpenseViewModel(
    private val expenseDao: ExpenseDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val expenseId: Int =
        savedStateHandle.get<Int>("expenseId") ?: 0

    private val _expense = MutableStateFlow<Expense?>(null)
    val expense: StateFlow<Expense?> = _expense

    var showSuccess by mutableStateOf(false)

    var amount by mutableStateOf("")
    var category by mutableStateOf("")
    var paymentMethod by mutableStateOf("")
    var date by mutableStateOf(System.currentTimeMillis())
    var note by mutableStateOf("")

    val categories = listOf("Food", "Transport", "Shopping", "Bills", "Other")
    val paymentMethods = listOf("Cash", "Card", "UPI")

    init {
        viewModelScope.launch {
            expenseDao.getAllExpenses().collect { list ->
                list.find { it.id == expenseId }?.let { e ->
                    _expense.value = e
                    amount = e.amount.toString()
                    category = e.category
                    paymentMethod = e.paymentMethod
                    date = e.date
                    note = e.note
                }
            }
        }
    }

    fun updateExpense() {
        val e = _expense.value ?: return
        val updated = e.copy(
            amount = amount.toDoubleOrNull() ?: e.amount,
            category = category,
            paymentMethod = paymentMethod,
            date = date,
            note = note
        )
        viewModelScope.launch {
            expenseDao.updateExpense(updated)
            showSuccess = true
        }
    }

    fun deleteExpense() {
        val e = _expense.value ?: return
        viewModelScope.launch {
            expenseDao.deleteExpense(e)
            showSuccess = true
        }
    }
}
