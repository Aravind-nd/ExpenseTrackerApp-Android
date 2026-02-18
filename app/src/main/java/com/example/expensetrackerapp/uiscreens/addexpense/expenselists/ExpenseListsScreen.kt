package com.example.expensetrackerapp.uiscreens.addexpense.expenselists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.SwipeToDismissBoxValue.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrackerapp.Expense
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    viewModel: ExpenseListViewModel,
    onEdit: (Expense) -> Unit,
    onClickItem: (Expense) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val expenses = uiState.expenses

    val categories = listOf(
        "All", "Food", "Transport",
        "Shopping", "Bills",
        "Entertainment", "Other"
    )

    var selectedCategory by remember { mutableStateOf("All") }
    var sortKey by remember { mutableStateOf("Amount") }
    var isAscending by remember { mutableStateOf(true) }

    // Filtering + Sorting
    val filteredExpenses = expenses.filter {
        selectedCategory == "All" || it.category == selectedCategory
    }

    val sortedExpenses = remember(filteredExpenses, sortKey, isAscending) {
        val sorted = when (sortKey) {
            "Amount" -> filteredExpenses.sortedBy { it.amount }
            "Date" -> filteredExpenses.sortedBy { it.date }
            else -> filteredExpenses
        }
        if (isAscending) sorted else sorted.reversed()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(Color(0xFF1976D2)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                "Expense List",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Category Scroll
        LazyRow(
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(categories) { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .background(
                            if (isSelected) Color(0xFF1976D2)
                            else MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(14.dp)
                        )
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }

        // Sort Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SegmentedButton(
                options = listOf("Amount", "Date"),
                selected = sortKey,
                onSelected = { sortKey = it }
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (isAscending) "Asc" else "Desc")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isAscending,
                    onCheckedChange = { isAscending = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Expense List
        if (sortedExpenses.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFFB0BEC5),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No expenses found",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color(0xFF90A4AE)
                    )
                    Text(
                        text = "in ${if (selectedCategory == "All") "your expenses" else selectedCategory}",
                        fontSize = 14.sp,
                        color = Color(0xFFB0BEC5)
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    sortedExpenses,
                    key = { it.id }
                ) { expense ->

                    val dismissState = rememberSwipeToDismissBoxState()

                    if (dismissState.currentValue == EndToStart) {
                        viewModel.deleteExpense(expense)
                    }

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {},
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true
                    ) {
                        ExpenseCard(
                            expense = expense,
                            onClick = { onClickItem(expense) },
                            onEdit = { onEdit(expense) }
                        )
                    }
                }
            }
        }

    }
}

////////////////////////////////////////////////////////////
// Segmented Button
////////////////////////////////////////////////////////////
@Composable
fun SegmentedButton(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    Row {
        options.forEach { option ->
            val isSelected = option == selected
            Box(
                modifier = Modifier
                    .background(
                        if (isSelected) Color(0xFF1976D2)
                        else MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(8.dp)
                    )
                    .clickable { onSelected(option) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = option,
                    color = if (isSelected) Color.White else Color.Black
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
    }
}

////////////////////////////////////////////////////////////
// Expense Card
////////////////////////////////////////////////////////////
@Composable
fun ExpenseCard(
    expense: Expense,
    onClick: (Expense) -> Unit,
    onEdit: (Expense) -> Unit
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(expense) }, // navigate to detail
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                tint = Color(0xFF1976D2)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(
                    expense.note ?: "No note",
                    fontWeight = FontWeight.Medium
                )

                Text(
                    expense.category ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                expense.date?.let {
                    Text(
                        dateFormat.format(Date(it)),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Text(
                "$${expense.amount.roundToInt()}",
                fontWeight = FontWeight.Bold
            )
        }
    }
}
