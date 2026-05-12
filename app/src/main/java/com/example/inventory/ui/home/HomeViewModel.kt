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

package com.example.inventory.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Craft
import com.example.inventory.data.CraftsRepository
import com.example.inventory.data.Sale
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve all crafts in the Room database.
 */
class HomeViewModel(private val craftsRepository: CraftsRepository) : ViewModel() {

    /**
     * Holds home ui state. The list of crafts are retrieved from [CraftsRepository] and mapped to
     * [HomeUiState]
     */
    val homeUiState: StateFlow<HomeUiState> =
        craftsRepository.getAllCraftsStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    fun quickBillSale(
        craftId: Int,
        selectedColor: String,
        onResult: (String) -> Unit
    ) {
        viewModelScope.launch {
            val craft = homeUiState.value.craftList.firstOrNull { it.id == craftId }
            if (craft == null) {
                onResult("Select a valid craft.")
                return@launch
            }
            if (craft.quantity <= 0) {
                onResult("Out of stock for ${craft.name}.")
                return@launch
            }

            craftsRepository.updateCraft(craft.copy(quantity = craft.quantity - 1))
            craftsRepository.insertSale(
                Sale(
                    craftName = craft.name,
                    color = selectedColor.ifBlank { craft.color },
                    price = craft.price,
                    quantity = 1
                )
            )
            onResult("Sale saved for ${craft.name}.")
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 60_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val craftList: List<Craft> = listOf())
