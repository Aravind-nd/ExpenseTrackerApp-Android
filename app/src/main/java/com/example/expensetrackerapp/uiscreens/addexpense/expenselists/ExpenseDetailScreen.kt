
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.expensetrackerapp.Expense
import com.example.expensetrackerapp.ExpenseDao
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDetailScreen(
    expenseId: Int,
    navController: NavController,
    expenseDao: ExpenseDao
) {
    var expense by remember { mutableStateOf<Expense?>(null) }

    // Fetch expense
    LaunchedEffect(expenseId) {
        expenseDao.getAllExpenses().collectLatest { list ->
            expense = list.find { it.id == expenseId }
        }
    }

    expense?.let { e ->

        val dateFormatted = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        ).format(Date(e.date))

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Expense Details", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // 🔵 Amount Highlight Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Color(0xFF3B82F6),
                                        Color(0xFF1E40AF)
                                    )
                                )
                            )
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Total Amount",
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "$ ${e.amount}",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // 📋 Details Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        DetailItem("Category", e.category)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        DetailItem("Payment Method", e.paymentMethod)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        DetailItem("Date", dateFormatted)
                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        DetailItem(
                            "Note",
                            if (e.note.isEmpty()) "No note added" else e.note
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // ✏️ Edit Button
                Button(
                    onClick = {
                        navController.navigate("edit_expense/${e.id}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Edit Expense", fontWeight = FontWeight.SemiBold)
                }
            }
        }

    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun DetailItem(title: String, value: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}
