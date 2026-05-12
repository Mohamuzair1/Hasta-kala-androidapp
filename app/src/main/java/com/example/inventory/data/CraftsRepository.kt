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

package com.example.inventory.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository that provides insert, update, delete, and retrieve of [Craft] from a given data source.
 */
interface CraftsRepository {
    /**
     * Retrieve all the crafts from the the given data source.
     */
    fun getAllCraftsStream(): Flow<List<Craft>>

    /**
     * Retrieve a craft from the given data source that matches with the [id].
     */
    fun getCraftStream(id: Int): Flow<Craft?>

    /**
     * Insert craft in the data source
     */
    suspend fun insertCraft(craft: Craft)

    /**
     * Delete craft from the data source
     */
    suspend fun deleteCraft(craft: Craft)

    /**
     * Update craft in the data source
     */
    suspend fun updateCraft(craft: Craft)

    /**
     * Insert sale in the data source
     */
    suspend fun insertSale(sale: Sale)

    /**
     * Get all sales
     */
    fun getAllSalesStream(): Flow<List<Sale>>

    /**
     * Get sales grouped by color
     */
    fun getSalesGroupedByColorStream(): Flow<List<ColorSaleCount>>

    /**
     * Get sales grouped by craft
     */
    fun getSalesGroupedByCraftStream(): Flow<List<CraftSaleCount>>
}
