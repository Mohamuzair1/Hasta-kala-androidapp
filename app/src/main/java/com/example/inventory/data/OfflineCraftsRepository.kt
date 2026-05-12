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

class OfflineCraftsRepository(
    private val craftDao: CraftDao,
    private val saleDao: SaleDao
) : CraftsRepository {
    override fun getAllCraftsStream(): Flow<List<Craft>> = craftDao.getAllCrafts()

    override fun getCraftStream(id: Int): Flow<Craft?> = craftDao.getCraft(id)

    override suspend fun insertCraft(craft: Craft) = craftDao.insert(craft)

    override suspend fun deleteCraft(craft: Craft) = craftDao.delete(craft)

    override suspend fun updateCraft(craft: Craft) = craftDao.update(craft)

    override suspend fun insertSale(sale: Sale) = saleDao.insert(sale)

    override fun getSalesGroupedByColorStream(): Flow<List<ColorSaleCount>> = saleDao.getSalesGroupedByColor()

    override fun getAllSalesStream(): Flow<List<Sale>> = saleDao.getAllSales()

    override fun getSalesGroupedByCraftStream(): Flow<List<CraftSaleCount>> = saleDao.getSalesGroupedByCraft()
}
