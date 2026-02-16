package com.example.expensetrackerapp.uiscreens.addexpense.EditExpense

import EditExpenseViewModel
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenseScreen(
    viewModel: EditExpenseViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    // ✅ Navigate after update/delete success
    LaunchedEffect(viewModel.showSuccess) {
        if (viewModel.showSuccess) {

            Toast.makeText(
                context,
                "✓ Expense Updated Successfully",
                Toast.LENGTH_SHORT
            ).show()

            viewModel.showSuccess = false

            // 🔥 Navigate to Dashboard and clear back stack
            navController.navigate("dashboard") {
                popUpTo("dashboard") { inclusive = true }
            }
        }
    }

    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance().apply { timeInMillis = viewModel.date }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, y, m, d ->
                val selected = Calendar.getInstance()
                selected.set(y, m, d)
                viewModel.date = selected.timeInMillis
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Expense", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3A8A)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Amount
            OutlinedTextField(
                value = viewModel.amount,
                onValueChange = { viewModel.amount = it },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth()
            )

            // Category
            var expanded by remember { mutableStateOf(false) }
            Box {
                OutlinedTextField(
                    value = viewModel.category,
                    onValueChange = {},
                    label = { Text("Category") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    readOnly = true
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    viewModel.categories.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                viewModel.category = cat
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Payment Method
            var payExpanded by remember { mutableStateOf(false) }
            Box {
                OutlinedTextField(
                    value = viewModel.paymentMethod,
                    onValueChange = {},
                    label = { Text("Payment Method") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { payExpanded = true },
                    readOnly = true
                )

                DropdownMenu(
                    expanded = payExpanded,
                    onDismissRequest = { payExpanded = false }
                ) {
                    viewModel.paymentMethods.forEach { method ->
                        DropdownMenuItem(
                            text = { Text(method) },
                            onClick = {
                                viewModel.paymentMethod = method
                                payExpanded = false
                            }
                        )
                    }
                }
            }

            // Date
            OutlinedTextField(
                value = dateFormat.format(Date(viewModel.date)),
                onValueChange = {},
                label = { Text("Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                readOnly = true
            )

            // Note
            OutlinedTextField(
                value = viewModel.note,
                onValueChange = { viewModel.note = it },
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Button(
                    onClick = { viewModel.updateExpense() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Update")
                }

                Button(
                    onClick = { viewModel.deleteExpense() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Delete")
                }
            }
        }
    }
}
