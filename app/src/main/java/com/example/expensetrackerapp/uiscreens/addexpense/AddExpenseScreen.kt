package com.example.expensetrackerapp.uiscreens.addexpense

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.shadow
import com.example.expensetrackerapp.Expense
import com.example.expensetrackerapp.ExpenseDao
import kotlinx.coroutines.flow.flowOf

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.CategoryTotal
import com.example.expensetrackerapp.DailyTotal
import com.example.expensetrackerapp.WeeklyTotal
import java.text.SimpleDateFormat
import java.util.*
import com.example.expensetrackerapp.uiscreens.addexpense.AddExpenseViewModel
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(viewModel: AddExpenseViewModel) {
    val context = LocalContext.current

    // Animation states
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val shimmerAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500),
            repeatMode = RepeatMode.Reverse
        ),
        label = ""
    )



    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Expense",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3A8A)
                ),
                modifier = Modifier.shadow(8.dp)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEFF6FF),
                            Color(0xFFDBEAFE),
                            Color.White
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Amount Card with gradient
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF3B82F6).copy(alpha = 0.1f),
                                        Color(0xFF60A5FA).copy(alpha = 0.05f)
                                    )
                                )
                            )
                    ) {
                        OutlinedTextField(
                            value = viewModel.amount,
                            onValueChange = { viewModel.amount = it },
                            label = { Text("Amount", fontWeight = FontWeight.SemiBold) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    "Amount",
                                    tint = Color(0xFF3B82F6)
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF3B82F6),
                                focusedLabelColor = Color(0xFF3B82F6),
                                cursorColor = Color(0xFF3B82F6)
                            )
                        )
                    }
                }

                // Category
                var categoryExpanded by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp))
                        .clickable { categoryExpanded = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.padding(12.dp)) {
                        OutlinedTextField(
                            value = viewModel.category,
                            onValueChange = {},
                            label = { Text("Category", fontWeight = FontWeight.SemiBold) },
                            leadingIcon = {
                                Text(
                                    text = viewModel.iconForCategory(viewModel.category),
                                    style = MaterialTheme.typography.titleLarge
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    "Dropdown",
                                    tint = Color(0xFF3B82F6)
                                )
                            },
                            readOnly = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color(0xFF93C5FD),
                                disabledTextColor = Color.Black,
                                disabledLabelColor = Color.Gray,
                                disabledLeadingIconColor = Color.Black,
                                disabledTrailingIconColor = Color(0xFF3B82F6)
                            )
                        )
                        DropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(Color.White)
                        ) {
                            viewModel.categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                viewModel.iconForCategory(cat),
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Text(cat, fontWeight = FontWeight.Medium)
                                        }
                                    },
                                    onClick = {
                                        viewModel.category = cat
                                        categoryExpanded = false
                                    },
                                    modifier = Modifier.background(
                                        if (viewModel.category == cat)
                                            Color(0xFF3B82F6).copy(alpha = 0.1f)
                                        else Color.Transparent
                                    )
                                )
                            }
                        }
                    }
                }

                // Date picker
                val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = viewModel.date

                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedCalendar = Calendar.getInstance()
                        selectedCalendar.set(year, month, dayOfMonth)
                        viewModel.date = selectedCalendar.timeInMillis
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp))
                        .clickable { datePickerDialog.show() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    OutlinedTextField(
                        value = dateFormat.format(Date(viewModel.date)),
                        onValueChange = {},
                        label = { Text("Date", fontWeight = FontWeight.SemiBold) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.DateRange,
                                "Date",
                                tint = Color(0xFF3B82F6)
                            )
                        },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = Color(0xFF93C5FD),
                            disabledTextColor = Color.Black,
                            disabledLabelColor = Color.Gray,
                            disabledLeadingIconColor = Color(0xFF3B82F6)
                        )
                    )
                }

                // Payment Method
                var paymentExpanded by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp))
                        .clickable { paymentExpanded = true },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Box(modifier = Modifier.padding(12.dp)) {
                        OutlinedTextField(
                            value = viewModel.paymentMethod,
                            onValueChange = {},
                            label = { Text("Payment Method", fontWeight = FontWeight.SemiBold) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.AccountBox,
                                    "Payment",
                                    tint = Color(0xFF3B82F6)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    "Dropdown",
                                    tint = Color(0xFF3B82F6)
                                )
                            },
                            readOnly = true,
                            enabled = false,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledBorderColor = Color(0xFF93C5FD),
                                disabledTextColor = Color.Black,
                                disabledLabelColor = Color.Gray,
                                disabledLeadingIconColor = Color(0xFF3B82F6),
                                disabledTrailingIconColor = Color(0xFF3B82F6)
                            )
                        )
                        DropdownMenu(
                            expanded = paymentExpanded,
                            onDismissRequest = { paymentExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(Color.White)
                        ) {
                            viewModel.paymentMethods.forEach { method ->
                                DropdownMenuItem(
                                    text = { Text(method, fontWeight = FontWeight.Medium) },
                                    onClick = {
                                        viewModel.paymentMethod = method
                                        paymentExpanded = false
                                    },
                                    modifier = Modifier.background(
                                        if (viewModel.paymentMethod == method)
                                            Color(0xFF3B82F6).copy(alpha = 0.1f)
                                        else Color.Transparent
                                    )
                                )
                            }
                        }
                    }
                }

                // Note
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    OutlinedTextField(
                        value = viewModel.note,
                        onValueChange = { viewModel.note = it },
                        label = { Text("Note (Optional)", fontWeight = FontWeight.SemiBold) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Edit,
                                "Note",
                                tint = Color(0xFF3B82F6)
                            )
                        },
                        minLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3B82F6),
                            unfocusedBorderColor = Color(0xFF93C5FD),
                            cursorColor = Color(0xFF3B82F6)
                        )
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Animated Save Button
                Button(
                    onClick = {
                        viewModel.addExpense()
                        if (viewModel.showSuccess) {
                            Toast.makeText(context, "✓ Expense Added!", Toast.LENGTH_SHORT).show()
                            viewModel.showSuccess = false
                        }
                    },
                    enabled = viewModel.amount.isNotEmpty() && viewModel.category != "Select Category",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .shadow(12.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6),
                        disabledContainerColor = Color(0xFF93C5FD)
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Save",
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(28.dp),
                            tint = Color.White
                        )
                        Text(
                            "Save Expense",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Suppress("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun AddExpenseScreenPreview() {
    // Create a mock DAO for preview
    val mockDao = object : ExpenseDao {
        override suspend fun insertExpense(expense: Expense) {}
        override fun getAllExpenses(): Flow<List<Expense>> = flowOf(emptyList())
        override fun getCategoryTotals(month: String, year: String): Flow<List<CategoryTotal>> = flowOf(emptyList())
        override fun getDailyTotals(month: String, year: String): Flow<List<DailyTotal>> = flowOf(emptyList())
        override fun getWeeklyTotals(month: String, year: String): Flow<List<WeeklyTotal>> = flowOf(emptyList())
        override fun getExpensesByMonth(month: String, year: String): Flow<List<Expense>> = flowOf(emptyList())
        override fun getExpensesSortedByAmount(): Flow<List<Expense>> = flowOf(emptyList())
        override fun getExpensesSortedByDate(): Flow<List<Expense>> = flowOf(emptyList())
        override suspend fun deleteExpense(expense: Expense) {}
        override suspend fun updateExpense(expense: Expense) {}
//
    }


    MaterialTheme {
        AddExpenseScreen(viewModel = AddExpenseViewModel(mockDao))
    }
}