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

package com.example.inventory.ui.craft

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.CraftsRepository
import com.example.inventory.data.Sale
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve, update and delete a craft from the [CraftsRepository]'s data source.
 */
class CraftDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val craftsRepository: CraftsRepository,
) : ViewModel() {

    private val craftId: Int = checkNotNull(savedStateHandle[CraftDetailsDestination.craftIdArg])

    /**
     * Holds the craft details ui state. The data is retrieved from [CraftsRepository] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<CraftDetailsUiState> =
        craftsRepository.getCraftStream(craftId)
            .filterNotNull()
            .map {
                CraftDetailsUiState(outOfStock = it.quantity <= 0, craftDetails = it.toCraftDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = CraftDetailsUiState()
            )

    /**
     * Reduces the craft quantity by one and update the [CraftsRepository]'s data source.
     */
    fun reduceQuantityByOne() {
        viewModelScope.launch {
            val currentCraft = uiState.value.craftDetails.toCraft()
            if (currentCraft.quantity > 0) {
                craftsRepository.updateCraft(currentCraft.copy(quantity = currentCraft.quantity - 1))
                // Save sale record
                val sale = Sale(
                    craftName = currentCraft.name,
                    color = currentCraft.color,
                    price = currentCraft.price,
                    quantity = 1
                )
                craftsRepository.insertSale(sale)
            }
        }
    }

    /**
     * Deletes the craft from the [CraftsRepository]'s data source.
     */
    suspend fun deleteCraft() {
        craftsRepository.deleteCraft(uiState.value.craftDetails.toCraft())
    }

    companion object {
        private const val TIMEOUT_MILLIS = 60_000L
    }
}

/**
 * UI state for CraftDetailsScreen
 */
data class CraftDetailsUiState(
    val outOfStock: Boolean = true,
    val craftDetails: CraftDetails = CraftDetails()
)
