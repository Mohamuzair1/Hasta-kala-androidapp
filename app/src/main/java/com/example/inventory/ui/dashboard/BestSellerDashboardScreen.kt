/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.dashboard

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.ui.AppViewModelProvider
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BestSellerDashboardScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SalesDashboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val allSales by viewModel.allSales.collectAsState()
    val totalEarnings by viewModel.totalEarnings.collectAsState()
    val weeklyEarnings by viewModel.weeklyEarnings.collectAsState()
    val monthlyEarnings by viewModel.monthlyEarnings.collectAsState()
    val salesByColor by viewModel.salesByColor.collectAsState()

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = "Sales History",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        SalesHistoryBody(
            allSales = allSales,
            totalEarnings = totalEarnings,
            weeklyEarnings = weeklyEarnings,
            monthlyEarnings = monthlyEarnings,
            salesByColor = salesByColor,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}

@Composable
private fun SalesHistoryBody(
    allSales: List<SaleUiState>,
    totalEarnings: Double,
    weeklyEarnings: Double,
    monthlyEarnings: Double,
    salesByColor: List<ColorSalesUiState>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF8F0), // Warm cream
                            Color(0xFFF5E6D3), // Light beige
                            Color(0xFFE8D5B7)  // Sandalwood
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFD4A574), // Terracotta
                                Color(0xFFB85C38), // Maroon
                                Color(0xFF8B4513)  // Saddle brown
                            )
                        )
                    )
                    .padding(vertical = 24.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "📊",
                    fontSize = 48.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Sales History",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Total Earnings: ₹${String.format(Locale.US, "%.2f", totalEarnings)}",
                    fontSize = 16.sp,
                    color = Color(0xFFFFF8DC),
                    textAlign = TextAlign.Center
                )
            }

            if (allSales.isEmpty() && salesByColor.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "📈",
                        fontSize = 64.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "No sales data yet",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF8B4513),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Start selling crafts to see history",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFA0826D)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        EarningsSummaryCard(
                            weeklyEarnings = weeklyEarnings,
                            monthlyEarnings = monthlyEarnings
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    if (salesByColor.isNotEmpty()) {
                        item {
                            ColorSalesPieChartCard(salesByColor = salesByColor)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                    items(allSales) { sale ->
                        SaleItem(sale = sale)
                    }
                }
            }
        }
    }
}

@Composable
private fun EarningsSummaryCard(
    weeklyEarnings: Double,
    monthlyEarnings: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Income Log",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "This Week: ₹${String.format(Locale.US, "%.2f", weeklyEarnings)}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF2C2C2C)
            )
            Text(
                text = "This Month: ₹${String.format(Locale.US, "%.2f", monthlyEarnings)}",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF2C2C2C)
            )
        }
    }
}

@Composable
private fun ColorSalesPieChartCard(
    salesByColor: List<ColorSalesUiState>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Best Selling Colors",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8B4513)
            )
            Spacer(modifier = Modifier.height(8.dp))
            AndroidView(
                factory = { ctx ->
                    PieChart(ctx).apply {
                        description.isEnabled = false
                        setUsePercentValues(true)
                        setDrawEntryLabels(false)
                        legend.textSize = 12f
                        legend.textColor = android.graphics.Color.parseColor("#5D2F0F")
                        setCenterTextSize(14f)
                        centerText = "Color Share"
                    }
                },
                update = { chart ->
                    val entries = salesByColor.map { PieEntry(it.percentage, it.color) }
                    val dataSet = PieDataSet(entries, "Color Sales").apply {
                        colors = salesByColor.map { getColorForName(it.color) }
                        valueTextColor = android.graphics.Color.WHITE
                        valueTextSize = 12f
                        sliceSpace = 2f
                    }
                    chart.data = PieData(dataSet)
                    chart.invalidate()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            )
        }
    }
}

private fun getColorForName(name: String): Int {
    return when (name.lowercase()) {
        "red" -> android.graphics.Color.parseColor("#DC143C")
        "blue" -> android.graphics.Color.parseColor("#4169E1")
        "green" -> android.graphics.Color.parseColor("#228B22")
        "yellow" -> android.graphics.Color.parseColor("#FFD700")
        "purple" -> android.graphics.Color.parseColor("#8B008B")
        "orange" -> android.graphics.Color.parseColor("#FF8C00")
        "brown" -> android.graphics.Color.parseColor("#8B4513")
        "maroon" -> android.graphics.Color.parseColor("#800000")
        else -> android.graphics.Color.parseColor("#D4A574")
    }
}

@Composable
private fun SaleItem(
    sale: SaleUiState,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${sale.productName} (${sale.color})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C2C2C)
                )
                Text(
                    text = "₹${String.format(Locale.US, "%.2f", sale.totalEarnings)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4A574)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Quantity: ${sale.quantitySold}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666)
                )
                Text(
                    text = sale.timeDate,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}
