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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostSellingDashboardScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MostSellingDashboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val mostSellingCrafts by viewModel.mostSellingCrafts.collectAsState()

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = "Most Selling Crafts",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        modifier = modifier,
    ) { innerPadding ->
        MostSellingDashboardBody(
            mostSellingCrafts = mostSellingCrafts,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun MostSellingDashboardBody(
    mostSellingCrafts: List<MostSellingCraftUiState>,
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
                    text = "🏆",
                    fontSize = 48.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Most Selling Crafts",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Top Performing Products",
                    fontSize = 16.sp,
                    color = Color(0xFFFFF8DC),
                    textAlign = TextAlign.Center
                )
            }

            if (mostSellingCrafts.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "📊",
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
                        text = "Start selling crafts to see rankings",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFA0826D)
                    )
                }
            } else {
                MostSellingPieChart(
                    mostSellingCrafts = mostSellingCrafts,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun MostSellingPieChart(
    mostSellingCrafts: List<MostSellingCraftUiState>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            PieChart(ctx).apply {
                description.isEnabled = false
                setDrawEntryLabels(false)
                legend.isEnabled = true
                legend.textSize = 14f
                legend.textColor = android.graphics.Color.parseColor("#8B4513")
                setHoleColor(android.graphics.Color.parseColor("#FFF8F0"))
                holeRadius = 40f
                transparentCircleRadius = 45f
                setDrawCenterText(true)
                centerText = "Sales by Craft"
                setCenterTextSize(16f)
                setCenterTextColor(android.graphics.Color.parseColor("#D4A574"))
            }
        },
        update = { pieChart ->
            val entries = mostSellingCrafts.map { craft ->
                PieEntry(craft.totalQuantity.toFloat(), craft.craftName)
            }

            val dataSet = PieDataSet(entries, "Sales").apply {
                colors = getChartColors(mostSellingCrafts.size)
                valueTextSize = 14f
                valueTextColor = android.graphics.Color.WHITE
                sliceSpace = 2f
            }

            val data = PieData(dataSet)
            pieChart.data = data
            pieChart.invalidate()
        },
        modifier = modifier
    )
}

private fun getChartColors(count: Int): List<Int> {
    val baseColors = listOf(
        android.graphics.Color.parseColor("#DC143C"), // Crimson
        android.graphics.Color.parseColor("#4169E1"), // Royal Blue
        android.graphics.Color.parseColor("#228B22"), // Forest Green
        android.graphics.Color.parseColor("#FFD700"), // Gold
        android.graphics.Color.parseColor("#8B008B"), // Dark Magenta
        android.graphics.Color.parseColor("#FF69B4"), // Hot Pink
        android.graphics.Color.parseColor("#FF8C00"), // Dark Orange
        android.graphics.Color.parseColor("#2F2F2F"), // Dark Gray
        android.graphics.Color.parseColor("#F5F5F5"), // White Smoke
        android.graphics.Color.parseColor("#8B4513"), // Saddle Brown
        android.graphics.Color.parseColor("#800000"), // Maroon
        android.graphics.Color.parseColor("#F5F5DC"), // Beige
    )

    return if (count <= baseColors.size) {
        baseColors.take(count)
    } else {
        // If more colors needed, repeat the pattern
        val repeated = mutableListOf<Int>()
        for (i in 0 until count) {
            repeated.add(baseColors[i % baseColors.size])
        }
        repeated
    }
}
