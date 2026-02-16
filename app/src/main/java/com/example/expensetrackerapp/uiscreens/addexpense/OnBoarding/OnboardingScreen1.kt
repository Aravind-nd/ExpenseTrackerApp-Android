package com.example.expensetrackerapp.uiscreens.addexpense.OnBoarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetrackerapp.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    var currentPage by remember { mutableStateOf(0) }

    val pages = listOf(
        OnboardingPageData(
            title = "Welcome to",
            subtitle = "Expense Tracker",
            features = listOf("Track your Spending", "Categorise Expenses", "Gain Insights"),
            imageHeight = 400.dp
        ),
        OnboardingPageData(
            title = "Insights & Reports",
            subtitle = "Understand your spending habits",
            features = listOf("View Spending by Category", "Analyze Daily & Monthly Trends", "Get Tips to Save Money"),
            imageHeight = 350.dp
        )
    )

    val page = pages[currentPage]

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Dark overlay for better contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f))
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Titles
            Text(page.title, fontSize = 32.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSecondary)
            Spacer(modifier = Modifier.height(8.dp))
            Text(page.subtitle, fontSize = 20.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSecondary)

            Spacer(modifier = Modifier.height(24.dp))

            // Logo / Image
            Image(
                painter = painterResource(id = R.drawable.logo2),
                contentDescription = null,
                modifier = Modifier
                    .height(page.imageHeight)
                    .fillMaxWidth(),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.weight(1f))

            // Feature List
            FeatureList(page.features)

            Spacer(modifier = Modifier.height(24.dp))

            // Buttons
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = {
                        if (currentPage < pages.size - 1) currentPage++ else onFinish()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp)
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color(0xFFFFD600)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (currentPage < pages.size - 1) "Next" else "Get Started", color = MaterialTheme.colorScheme.onSecondary)
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (currentPage == 0) {
                    Text(
                        text = "Skip",
                        color = androidx.compose.ui.graphics.Color(0xFFFFD600),
                        modifier = Modifier.clickable { onFinish() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Page Indicators
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    pages.forEachIndexed { index, _ ->
                        val size by animateDpAsState(targetValue = if (index == currentPage) 12.dp else 8.dp)
                        Box(
                            modifier = Modifier
                                .size(size)
                                .background(
                                    color = if (index == currentPage) androidx.compose.ui.graphics.Color(0xFFFFD600) else androidx.compose.ui.graphics.Color(0xFFFFD600).copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

data class OnboardingPageData(
    val title: String,
    val subtitle: String,
    val features: List<String>,
    val imageHeight: androidx.compose.ui.unit.Dp
)

@Composable
fun FeatureList(features: List<String>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 32.dp)
    ) {
        features.forEach { text ->
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.size(28.dp)
                )
                Text(text = text, color = MaterialTheme.colorScheme.onSecondary, fontSize = 18.sp)
            }
        }
    }
}
