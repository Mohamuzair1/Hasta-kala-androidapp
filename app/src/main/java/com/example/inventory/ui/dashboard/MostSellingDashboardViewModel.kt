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
import com.example.inventory.data.CraftsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import androidx.lifecycle.viewModelScope

/**
 * ViewModel to retrieve most selling crafts data for the dashboard.
 */
class MostSellingDashboardViewModel(
    private val craftsRepository: CraftsRepository
) : ViewModel() {

    val mostSellingCrafts: StateFlow<List<MostSellingCraftUiState>> = craftsRepository.getSalesGroupedByCraftStream()
        .map { crafts ->
            val totalQuantity = crafts.sumOf { it.totalQuantity }
            crafts.map { craft ->
                MostSellingCraftUiState(
                    craftName = craft.craftName,
                    totalQuantity = craft.totalQuantity,
                    percentage = if (totalQuantity > 0) (craft.totalQuantity.toFloat() / totalQuantity * 100) else 0f
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(60000),
            initialValue = emptyList()
        )
}

data class MostSellingCraftUiState(
    val craftName: String,
    val totalQuantity: Int,
    val percentage: Float
)
