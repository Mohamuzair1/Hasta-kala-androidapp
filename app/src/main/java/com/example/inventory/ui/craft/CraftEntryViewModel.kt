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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.inventory.data.Craft
import com.example.inventory.data.CraftsRepository
import java.util.Locale

/**
 * ViewModel to validate and insert crafts in the Room database.
 */
class CraftEntryViewModel(private val craftsRepository: CraftsRepository) : ViewModel() {

    /**
     * Holds current craft ui state
     */
    var craftUiState by mutableStateOf(CraftUiState())
        private set

    /**
     * Updates the [craftUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(craftDetails: CraftDetails) {
        craftUiState =
            CraftUiState(craftDetails = craftDetails, isEntryValid = validateInput(craftDetails))
    }

    /**
     * Inserts a [Craft] in the Room database
     */
    suspend fun saveCraft() {
        if (validateInput()) {
            craftsRepository.insertCraft(craftUiState.craftDetails.toCraft())
        }
    }

    private fun validateInput(uiState: CraftDetails = craftUiState.craftDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for a Craft.
 */
data class CraftUiState(
    val craftDetails: CraftDetails = CraftDetails(),
    val isEntryValid: Boolean = false
)

data class CraftDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
    val color: String = ""
)

/**
 * Extension function to convert [CraftUiState] to [Craft]. If the value of [CraftDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [CraftUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun CraftDetails.toCraft(): Craft = Craft(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0,
    color = color
)

fun Craft.formatedPrice(): String {
    return "₹" + String.format(Locale.US, "%.2f", price)
}

/**
 * Extension function to convert [Craft] to [CraftUiState]
 */
fun Craft.toCraftUiState(isEntryValid: Boolean = false): CraftUiState = CraftUiState(
    craftDetails = this.toCraftDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Craft] to [CraftDetails]
 */
fun Craft.toCraftDetails(): CraftDetails = CraftDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString(),
    color = color
)

