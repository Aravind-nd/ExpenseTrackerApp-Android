package com.example.expensetrackerapp.uiscreens.addexpense.analytics

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.example.expensetrackerapp.CategoryTotal
import com.example.expensetrackerapp.WeeklyTotal
import java.util.Calendar
import kotlin.math.roundToInt

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel) {
    
    val uiState by viewModel.uiState.collectAsState()
    val selectedMonth = viewModel.selectedMonth
    val selectedYear = viewModel.selectedYear
    
    val categoryTotals = uiState.categoryTotals
    val weeklyTotals = uiState.weeklyTotals
    val totalSpending = uiState.totalSpending
    val avgSpending = uiState.avgSpending
    val topCategory = uiState.topCategory

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Analytics for:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(8.dp))

            MonthYearPicker(
                selectedMonth,
                selectedYear,
                { viewModel.updateMonth(it) },
                { viewModel.updateYear(it) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnalyticsCard("Spending by Category") {
            if (categoryTotals.isEmpty()) {
                Text("No expenses for this month")
            } else {
                CategoryPieChart(categoryTotals)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnalyticsCard("Weekly Spending") {
            if (weeklyTotals.isEmpty()) {
                Text("No expenses for this month")
            } else {
                WeeklyLineChart(weeklyTotals)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnalyticsCard("Summary") {
            SpendingSummary(
                totalSpending,
                avgSpending,
                topCategory
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

////////////////////////////////////////////////////////////
// Month + Year Picker
////////////////////////////////////////////////////////////

@Composable
fun MonthYearPicker(
    selectedMonth: Int,
    selectedYear: Int,
    onMonthChange: (Int) -> Unit,
    onYearChange: (Int) -> Unit
) {

    val months = listOf(
        "Jan","Feb","Mar","Apr","May","Jun",
        "Jul","Aug","Sep","Oct","Nov","Dec"
    )

    Row(verticalAlignment = Alignment.CenterVertically) {

        var expandedMonth by remember { mutableStateOf(false) }

        Box {
            Text(
                text = months[selectedMonth],
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .clickable { expandedMonth = true }
            )

            DropdownMenu(
                expanded = expandedMonth,
                onDismissRequest = { expandedMonth = false }
            ) {
                months.forEachIndexed { index, month ->
                    DropdownMenuItem(
                        text = { Text(month) },
                        onClick = {
                            onMonthChange(index)
                            expandedMonth = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        var expandedYear by remember { mutableStateOf(false) }
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (currentYear downTo currentYear - 10).toList()

        Box {
            Text(
                text = selectedYear.toString(),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .clickable { expandedYear = true }
            )

            DropdownMenu(
                expanded = expandedYear,
                onDismissRequest = { expandedYear = false }
            ) {
                years.forEach { year ->
                    DropdownMenuItem(
                        text = { Text(year.toString()) },
                        onClick = {
                            onYearChange(year)
                            expandedYear = false
                        }
                    )
                }
            }
        }
    }
}

////////////////////////////////////////////////////////////
// Analytics Card
////////////////////////////////////////////////////////////

@Composable
fun AnalyticsCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            content()
        }
    }
}

////////////////////////////////////////////////////////////
// Clean Donut Pie Chart + Legend
////////////////////////////////////////////////////////////

//@Composable
//fun CategoryPieChart(data: List<CategoryTotal>) {
//
//    val colors = listOf(
//        Color(0xFF4CAF50),
//        Color(0xFF2196F3),
//        Color(0xFFFF9800),
//        Color(0xFFE91E63),
//        Color(0xFF9C27B0),
//        Color(0xFF009688),
//        Color(0xFF795548),
//        Color(0xFF607D8B)
//    )
//
//    val total = data.sumOf { it.total }.takeIf { it > 0 } ?: 1.0
//
//    val colorMap = data.mapIndexed { index, item ->
//        item.category to colors[index % colors.size]
//    }.toMap()
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(280.dp)
//            .padding(vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        AndroidView(
//            modifier = Modifier.weight(1.6f),
//            factory = { context ->
//
//                PieChart(context).apply {
//
//                    val entries = data.map {
//                        PieEntry(it.total.toFloat())
//                    }
//
//                    val dataSet = PieDataSet(entries, "").apply {
//                        this.colors = data.map {
//                            colorMap[it.category]?.toArgb() ?: AndroidColor.GRAY
//                        }
//                        sliceSpace = 0f
//                        setDrawValues(false)
//                    }
//
//                    this.data = PieData(dataSet)
//
//                    setDrawEntryLabels(false)
//                    setDrawHoleEnabled(true)
//
//                    holeRadius = 65f
//                    transparentCircleRadius = 70f
//
//                    description.isEnabled = false
//                    legend.isEnabled = false
//
//                    animateY(900)
//                    invalidate()
//                }
//            }
//        )
//
//        Spacer(modifier = Modifier.width(16.dp))
//
//        LazyColumn(
//            modifier = Modifier.weight(1.4f),
//            verticalArrangement = Arrangement.spacedBy(10.dp)
//        ) {
//            items(data) { item ->
//
//                val percent = ((item.total / total) * 100).roundToInt()
//
//                Row(verticalAlignment = Alignment.CenterVertically) {
//
//                    Box(
//                        modifier = Modifier
//                            .size(14.dp)
//                            .background(
//                                colorMap[item.category] ?: Color.Gray,
//                                RoundedCornerShape(4.dp)
//                            )
//                    )
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    Text(
//                        text = item.category,
//                        modifier = Modifier.weight(1f)
//                    )
//
//                    Text("$percent%")
//                }
//            }
//        }
//    }
//}

@Composable
fun CategoryPieChart(data: List<CategoryTotal>) {

    val colors = listOf(
        Color(0xFF4CAF50),
        Color(0xFF2196F3),
        Color(0xFFFF9800),
        Color(0xFFE91E63),
        Color(0xFF9C27B0),
        Color(0xFF009688),
        Color(0xFF795548),
        Color(0xFF607D8B)
    )

    val total = data.sumOf { it.total }.takeIf { it > 0 } ?: 1.0

    val colorMap = data.mapIndexed { index, item ->
        item.category to colors[index % colors.size]
    }.toMap()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp) // 👈 Bigger height
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        ////////////////////////////////
        // BIG DONUT CHART
        ////////////////////////////////

        AndroidView(
            modifier = Modifier
                .weight(2.2f) // 👈 More space for chart
                .fillMaxHeight(),
            factory = { context ->

                PieChart(context).apply {

                    val entries = data.map {
                        PieEntry(it.total.toFloat())
                    }

                    val dataSet = PieDataSet(entries, "").apply {
                        this.colors = data.map {
                            colorMap[it.category]?.toArgb() ?: AndroidColor.GRAY
                        }

                        sliceSpace = 0f // 👈 NO GAPS
                        setDrawValues(false)
                    }

                    this.data = PieData(dataSet)

                    setDrawEntryLabels(false)
                    setUsePercentValues(false)

                    setDrawHoleEnabled(true)

                    holeRadius = 68f // 👈 slightly larger inner hole
                    transparentCircleRadius = 72f

                    description.isEnabled = false
                    legend.isEnabled = false

                    setExtraOffsets(0f, 0f, 0f, 0f)

                    animateY(900)
                    invalidate()
                }
            }
        )

        Spacer(modifier = Modifier.width(20.dp))

        ////////////////////////////////
        // LEGEND
        ////////////////////////////////

        LazyColumn(
            modifier = Modifier
                .weight(1.3f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(data) { item ->

                val percent = ((item.total / total) * 100).roundToInt()

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                colorMap[item.category] ?: Color.Gray,
                                RoundedCornerShape(4.dp)
                            )
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = item.category,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "$percent%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

////////////////////////////////////////////////////////////
// Weekly Line Chart
////////////////////////////////////////////////////////////

@Composable
fun WeeklyLineChart(data: List<WeeklyTotal>) {

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        factory = { context ->

            LineChart(context).apply {

                val entries = data.mapIndexed { index, item ->
                    Entry(index.toFloat(), item.total.toFloat())
                }

                val dataSet = LineDataSet(entries, "").apply {
                    color = AndroidColor.parseColor("#3F51B5")
                    setCircleColor(AndroidColor.parseColor("#3F51B5"))
                    lineWidth = 3f
                    circleRadius = 5f
                    setDrawFilled(true)
                    fillColor = AndroidColor.parseColor("#C5CAE9")
                }

                this.data = LineData(dataSet)

                xAxis.apply {
                    granularity = 1f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return "W${value.toInt() + 1}"
                        }
                    }
                    position = XAxis.XAxisPosition.BOTTOM
                }

                axisRight.isEnabled = false
                description.isEnabled = false
                legend.isEnabled = false

                animateY(800)
                invalidate()
            }
        }
    )
}

////////////////////////////////////////////////////////////
// Summary
////////////////////////////////////////////////////////////

@Composable
fun SpendingSummary(
    total: Double,
    avg: Double,
    topCategory: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

            SummaryTile(
                "Top Category",
                topCategory,
                Icons.Default.Star,
                Color(0xFFFF9800),
                Modifier.weight(1f)
            )

            SummaryTile(
                "Avg / Week",
                "$%.2f".format(avg),
                Icons.Default.ShowChart,
                Color.Blue,
                Modifier.weight(1f)
            )
        }

        SummaryTile(
            "Total Spending",
            "$%.2f".format(total),
            Icons.Default.CreditCard,
            Color(0xFF4CAF50),
            Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SummaryTile(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null, tint = color)
            Text(title, style = MaterialTheme.typography.bodySmall)
            Text(value, style = MaterialTheme.typography.titleMedium)
        }
    }
}
