package com.example.expensetrackerapp

import EditExpenseViewModel
import ExpenseDetailScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.expensetrackerapp.ui.theme.ExpenseTrackerAppTheme
import com.example.expensetrackerapp.uiscreens.addexpense.*
import com.example.expensetrackerapp.uiscreens.addexpense.EditExpense.EditExpenseScreen

import com.example.expensetrackerapp.uiscreens.addexpense.OnBoarding.OnboardingScreen
import com.example.expensetrackerapp.uiscreens.addexpense.analytics.AnalyticsScreen
import com.example.expensetrackerapp.uiscreens.addexpense.dashboard.DashboardScreen

import com.example.expensetrackerapp.uiscreens.addexpense.expenselists.ExpenseListScreen
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = ExpenseDatabase.getDatabase(applicationContext)
        val dao = database.expenseDao()

        setContent {
            ExpenseTrackerAppTheme {
                AppNavigation(dao)
            }
        }
    }

    @Composable
    fun AppNavigation(dao: ExpenseDao) {
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()

        // Check if onboarding has been seen
        var hasSeenOnboarding by remember { mutableStateOf(false) }

        LaunchedEffect(key1 = true) {
            hasSeenOnboarding = UserPreferences.hasSeenOnboarding(applicationContext)
        }

        NavHost(
            navController = navController,
            startDestination = if (hasSeenOnboarding) "dashboard" else "onboarding"
        ) {

            // Onboarding Screen
            composable("onboarding") {
                OnboardingScreen(
                    onFinish = {
                        hasSeenOnboarding = true
                        UserPreferences.setHasSeenOnboarding(applicationContext, true)
                        navController.navigate("dashboard") {
                            popUpTo("onboarding") { inclusive = true }
                        }
                    }
                )
            }

            // Dashboard
            composable("dashboard") {
                val currentDate = System.currentTimeMillis()
                val calendar = java.util.Calendar.getInstance()
                calendar.timeInMillis = currentDate
                val currentMonth = (calendar.get(java.util.Calendar.MONTH) + 1).toString().padStart(2, '0')
                val currentYear = calendar.get(java.util.Calendar.YEAR).toString()

                val dailyTotals by dao.getDailyTotals(currentMonth, currentYear).collectAsState(initial = emptyList())
                val allExpenses by dao.getAllExpenses().collectAsState(initial = emptyList())

                val todayTotal = dailyTotals.lastOrNull()?.total ?: 0.0
                val monthTotal = dailyTotals.sumOf { it.total }
                val recentExpenses = allExpenses.take(5)

                DashboardScreen(
                    todayTotal = todayTotal,
                    monthTotal = monthTotal,
                    recentExpenses = recentExpenses,
                    onAddExpenseClick = { navController.navigate("add_expense") },
                    onExpenseListClick = { navController.navigate("expense_list") },
                    onAnalyticsClick = { navController.navigate("analytics") },
                    onEditExpenseClick = { expense ->
                        navController.navigate("edit_expense/${expense.id}")
                    }
                )
            }

            // Add Expense
            composable("add_expense") {
                val addViewModel: AddExpenseViewModel = viewModel(
                    factory = AddExpenseViewModelFactory(dao)
                )
                AddExpenseScreen(viewModel = addViewModel)
            }

            // Edit Expense
            composable(
                route = "edit_expense/{expenseId}",
                arguments = listOf(navArgument("expenseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val expenseId = backStackEntry.arguments?.getInt("expenseId") ?: return@composable
                val editViewModel: EditExpenseViewModel = viewModel(
                    factory = EditExpenseViewModelFactory(dao, expenseId, backStackEntry.savedStateHandle)
                )
                EditExpenseScreen(editViewModel, navController)
            }

            // Analytics
            composable("analytics") {
                AnalyticsScreen(dao)
            }

            // Expense List
            composable("expense_list") {
                val expenses by dao.getAllExpenses().collectAsState(initial = emptyList())
                ExpenseListScreen(
                    expenses = expenses,
                    onEdit = { expense ->
                        navController.navigate("edit_expense/${expense.id}")
                    },
                    onDelete = { expense ->
                        scope.launch { dao.deleteExpense(expense) }
                    },
                    onClickItem = { expense ->
                        navController.navigate("expense_detail/${expense.id}")
                    }
                )
            }

            // Expense Detail
            composable(
                route = "expense_detail/{expenseId}",
                arguments = listOf(navArgument("expenseId") { type = NavType.IntType })
            ) { backStackEntry ->
                val expenseId = backStackEntry.arguments?.getInt("expenseId") ?: return@composable
                ExpenseDetailScreen(expenseId, navController, dao)
            }
        }
    }
}

// -------------------------
// ViewModel Factories
// -------------------------

class AddExpenseViewModelFactory(private val dao: ExpenseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddExpenseViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class EditExpenseViewModelFactory(
    private val dao: ExpenseDao,
    private val expenseId: Int,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditExpenseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditExpenseViewModel(dao, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

// -------------------------
// UserPreferences helper
// -------------------------

object UserPreferences {
    private const val PREFS_NAME = "expense_prefs"
    private const val ONBOARDING_KEY = "has_seen_onboarding"

    fun hasSeenOnboarding(context: android.content.Context): Boolean {
        return context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
            .getBoolean(ONBOARDING_KEY, false)
    }

    fun setHasSeenOnboarding(context: android.content.Context, seen: Boolean) {
        context.getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
            .edit().putBoolean(ONBOARDING_KEY, seen).apply()
    }
}
