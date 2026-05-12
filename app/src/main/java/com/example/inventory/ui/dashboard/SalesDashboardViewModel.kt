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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.CraftsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar

/**
 * ViewModel to retrieve sales data for the dashboard.
 */
class SalesDashboardViewModel(
    private val craftsRepository: CraftsRepository
) : ViewModel() {

    val allSales: StateFlow<List<SaleUiState>> = craftsRepository.getAllSalesStream()
        .map { sales ->
            sales.map { sale ->
                SaleUiState(
                    productName = sale.craftName,
                    color = sale.color,
                    quantitySold = sale.quantity,
                    saleTimestamp = sale.timestamp,
                    timeDate = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date(sale.timestamp)),
                    totalEarnings = sale.price * sale.quantity
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(60000),
            initialValue = emptyList()
        )

    val totalEarnings: StateFlow<Double> = allSales.map { sales ->
        sales.sumOf { it.totalEarnings }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(60000),
        initialValue = 0.0
    )

    val weeklyEarnings: StateFlow<Double> = allSales.map { sales ->
        val now = System.currentTimeMillis()
        val weekAgo = now - 7L * 24L * 60L * 60L * 1000L
        sales.filter { it.saleTimestamp >= weekAgo }.sumOf { it.totalEarnings }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(60000),
        initialValue = 0.0
    )

    val monthlyEarnings: StateFlow<Double> = allSales.map { sales ->
        val now = Calendar.getInstance()
        val currentMonth = now.get(Calendar.MONTH)
        val currentYear = now.get(Calendar.YEAR)
        sales.filter { sale ->
            val cal = Calendar.getInstance().apply { timeInMillis = sale.saleTimestamp }
            cal.get(Calendar.MONTH) == currentMonth && cal.get(Calendar.YEAR) == currentYear
        }.sumOf { it.totalEarnings }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(60000),
        initialValue = 0.0
    )

    val salesByColor: StateFlow<List<ColorSalesUiState>> =
        craftsRepository.getSalesGroupedByColorStream()
            .map { grouped ->
                val total = grouped.sumOf { it.count }.coerceAtLeast(1)
                grouped.map { colorCount ->
                    ColorSalesUiState(
                        color = colorCount.color,
                        count = colorCount.count,
                        percentage = (colorCount.count.toFloat() / total.toFloat()) * 100f
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(60000),
                initialValue = emptyList()
            )
}

data class SaleUiState(
    val productName: String,
    val color: String,
    val quantitySold: Int,
    val saleTimestamp: Long,
    val timeDate: String,
    val totalEarnings: Double
)

data class ColorSalesUiState(
    val color: String,
    val count: Int,
    val percentage: Float
)
