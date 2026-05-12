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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Database access object to access the Sales database
 */
@Dao
interface SaleDao {

    @Query("SELECT * from sales ORDER BY timestamp DESC")
    fun getAllSales(): Flow<List<Sale>>

    @Query("SELECT color, COUNT(*) as count FROM sales GROUP BY color ORDER BY count DESC")
    fun getSalesGroupedByColor(): Flow<List<ColorSaleCount>>

    // Specify the conflict strategy as IGNORE, when the user tries to add an
    // existing Sale into the database Room ignores the conflict.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(sale: Sale)

    @Query("SELECT craftName, SUM(quantity) as totalQuantity FROM sales GROUP BY craftName ORDER BY totalQuantity DESC")
    fun getSalesGroupedByCraft(): Flow<List<CraftSaleCount>>
}

/**
 * Data class to hold color and sale count for dashboard
 */
data class ColorSaleCount(
    val color: String,
    val count: Int
)

/**
 * Data class to hold craft name and total quantity sold for dashboard
 */
data class CraftSaleCount(
    val craftName: String,
    val totalQuantity: Int
)
