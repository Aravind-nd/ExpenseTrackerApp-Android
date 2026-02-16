//package com.example.expensetrackerapp.uiscreens.addexpense.dashboard
//
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.expensetrackerapp.Expense
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DashboardScreen(
//    todayTotal: Double,
//    monthTotal: Double,
//    recentExpenses: List<Expense>,
//    onAddExpenseClick: () -> Unit,
//    onExpenseListClick: () -> Unit
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF0F0F0))
//            .padding(25.dp)
//    ) {
//        Text(
//            text = "Dashboard",
//            fontWeight = FontWeight.Bold,
//            fontSize = 28.sp,
//            color = Color.Black
//        )
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        // -------------------------
//        // Greeting Card
//        // -------------------------
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFF3B82F6).copy(alpha = 0.15f), RoundedCornerShape(16.dp))
//                .padding(20.dp)
//        ) {
//            Text(
//                text = "Hello, Aravind 👋",
//                fontWeight = FontWeight.SemiBold,
//                fontSize = 20.sp,
//                color = Color.Black
//            )
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // -------------------------
//        // Summary Cards
//        // -------------------------
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            SummaryCard(title = "Today", value = "$$todayTotal", modifier = Modifier.weight(1f))
//            SummaryCard(title = "This Month", value = "$$monthTotal", modifier = Modifier.weight(1f))
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // -------------------------
//        // Recent Expenses
//        // -------------------------
//        Text(
//            text = "Recent Expenses",
//            fontWeight = FontWeight.SemiBold,
//            color = Color.Black,
//            fontSize = 18.sp
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        LazyColumn(
//            modifier = Modifier.weight(1f),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            contentPadding = PaddingValues(vertical = 4.dp)
//        ) {
//            items(recentExpenses.take(5)) { expense ->
//                ExpenseRow(expense)
//            }
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // -------------------------
//        // Bottom Buttons
//        // -------------------------
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            BottomButton(
//                icon = Icons.Default.Home,
//                label = "Dashboard",
//                color = Color(0xFF3B82F6),
//                modifier = Modifier.weight(1f)
//            ) { /* Already Dashboard */ }
//
//            BottomButton(
//                icon = Icons.Default.List,
//                label = "Expense List",
//                color = Color(0xFF10B981),
//                modifier = Modifier.weight(1f),
//                onClick = onExpenseListClick
//            )
//
//            BottomButton(
//                icon = Icons.Default.AddCircle,
//                label = "Add Expense",
//                color = Color(0xFFF97316),
//                modifier = Modifier.weight(1f),
//                onClick = onAddExpenseClick
//            )
//        }
//    }
//}
//
//// -------------------------
//// Summary Card
//// -------------------------
//@Composable
//fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
//    Card(
//        modifier = modifier
//            .shadow(4.dp, RoundedCornerShape(16.dp)),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
//            Text(value, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
//        }
//    }
//}
//
//// -------------------------
//// Expense Row
//// -------------------------
//@Composable
//fun ExpenseRow(expense: Expense) {
//    val (icon, tint) = categoryVisuals(expense.category)
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .shadow(2.dp, RoundedCornerShape(16.dp)),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDFD))
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(14.dp)
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Row(
//                modifier = Modifier.weight(1f),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(46.dp)
//                        .background(tint.copy(alpha = 0.15f), CircleShape),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        imageVector = icon,
//                        contentDescription = null,
//                        tint = tint
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(12.dp))
//
//                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
//                    Text(expense.note.ifEmpty { "No note" }, fontWeight = FontWeight.SemiBold)
//                    Text(expense.category, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280))
//                }
//            }
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Text("$${expense.amount}", fontWeight = FontWeight.SemiBold, color = tint)
//        }
//    }
//}
//
//// -------------------------
//// Category → Icon/Color
//// -------------------------
//private fun categoryVisuals(category: String): Pair<ImageVector, Color> {
//    return when (category.trim().lowercase()) {
//        "food" -> Icons.Default.ShoppingCart to Color(0xFFEF4444)
//        "transport" -> Icons.Default.List to Color(0xFFF59E0B)
//        "shopping" -> Icons.Default.ShoppingCart to Color(0xFF3B82F6)
//        "bills" -> Icons.Default.Home to Color(0xFF06B6D4)
//        "entertainment", "entretaiment" -> Icons.Default.AddCircle to Color(0xFF8B5CF6)
//        "other" -> Icons.Default.List to Color(0xFF64748B)
//        else -> Icons.Default.List to Color(0xFF64748B)
//    }
//}
//
//// -------------------------
//// Bottom Button
//// -------------------------
//@Composable
//fun BottomButton(
//    icon: androidx.compose.ui.graphics.vector.ImageVector,
//    label: String,
//    color: Color,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit = {}
//) {
//    Button(
//        onClick = onClick,
//        modifier = modifier,
//        colors = ButtonDefaults.buttonColors(containerColor = color.copy(alpha = 0.2f)),
//        shape = RoundedCornerShape(12.dp),
//        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 8.dp)
//    ) {
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            Icon(icon, contentDescription = null, tint = color)
//            Spacer(modifier = Modifier.height(6.dp))
//            Text(
//                label,
//                color = color,
//                fontWeight = FontWeight.SemiBold,
//                maxLines = 1
//            )
//        }
//    }
//}

package com.example.expensetrackerapp.uiscreens.addexpense.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrackerapp.Expense

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    todayTotal: Double,
    monthTotal: Double,
    recentExpenses: List<Expense>,
    onAddExpenseClick: () -> Unit,
    onExpenseListClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    onEditExpenseClick: (Expense) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
            .padding(16.dp)
    ) {
        // -------------------------
        // Top Row: Title + Analytics Button
        // -------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Dashboard",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Black
            )

            IconButton(onClick = { onAnalyticsClick() }) {
                Icon(
                    imageVector = Icons.Default.ShowChart,
                    contentDescription = "Analytics",
                    tint = Color(0xFF3B82F6)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // -------------------------
        // Greeting Card
        // -------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF3B82F6).copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Text(
                text = "Hello, Aravind 👋",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -------------------------
        // Summary Cards
        // -------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryCard(title = "Today", value = "$%.2f".format(todayTotal), modifier = Modifier.weight(1f))
            SummaryCard(title = "This Month", value = "$%.2f".format(monthTotal), modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // -------------------------
        // Recent Expenses
        // -------------------------
        Text(
            text = "Recent Expenses",
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (recentExpenses.isEmpty()) {
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
                        text = "No expenses yet",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color(0xFF90A4AE)
                    )
                    Text(
                        text = "Start tracking your expenses to see them here",
                        fontSize = 14.sp,
                        color = Color(0xFFB0BEC5)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(recentExpenses.take(5)) { expense ->
                    ExpenseRow(expense) { clickedExpense ->
                        onEditExpenseClick(clickedExpense)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // -------------------------
        // Bottom Buttons
        // -------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            BottomButton(
                icon = Icons.Default.Home,
                label = "Dashboard",
                color = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            ) { /* Already Dashboard */ }

            BottomButton(
                icon = Icons.Default.List,
                label = "Expense List",
                color = Color(0xFF10B981),
                modifier = Modifier.weight(1f),
                onClick = onExpenseListClick
            )

            BottomButton(
                icon = Icons.Default.AddCircle,
                label = "Add Expense",
                color = Color(0xFFF97316),
                modifier = Modifier.weight(1f),
                onClick = onAddExpenseClick
            )
        }
    }
}

// -------------------------
// Summary Card
// -------------------------
@Composable
fun SummaryCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(value, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}

// -------------------------
// Expense Row (clickable)
// -------------------------
@Composable
fun ExpenseRow(expense: Expense, onClick: (Expense) -> Unit) {
    val (icon, tint) = categoryVisuals(expense.category)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(16.dp))
            .clickable { onClick(expense) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFDFDFD))
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(tint.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = tint
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(expense.note.ifEmpty { "No note" }, fontWeight = FontWeight.SemiBold)
                    Text(expense.category, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6B7280))
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text("$${expense.amount}", fontWeight = FontWeight.SemiBold, color = tint)
        }
    }
}

// -------------------------
// Category → Icon/Color
// -------------------------
private fun categoryVisuals(category: String): Pair<ImageVector, Color> {
    return when (category.trim().lowercase()) {
        "food" -> Icons.Default.ShoppingCart to Color(0xFFEF4444)
        "transport" -> Icons.Default.List to Color(0xFFF59E0B)
        "shopping" -> Icons.Default.ShoppingCart to Color(0xFF3B82F6)
        "bills" -> Icons.Default.Home to Color(0xFF06B6D4)
        "entertainment", "entretaiment" -> Icons.Default.AddCircle to Color(0xFF8B5CF6)
        "other" -> Icons.Default.List to Color(0xFF64748B)
        else -> Icons.Default.List to Color(0xFF64748B)
    }
}

// -------------------------
// Bottom Button
// -------------------------
@Composable
fun BottomButton(
    icon: ImageVector,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = color.copy(alpha = 0.2f)),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(vertical = 10.dp, horizontal = 8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.height(6.dp))
            Text(label, color = color, fontWeight = FontWeight.SemiBold, maxLines = 1)
        }
    }
}
