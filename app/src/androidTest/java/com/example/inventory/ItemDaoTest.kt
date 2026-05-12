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

package com.example.inventory

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.Craft
import com.example.inventory.data.CraftDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CraftDaoTest {

    private lateinit var craftDao: CraftDao
    private lateinit var inventoryDatabase: InventoryDatabase
    private val craft1 = Craft(1, "Apples", 10.0, 20, "Red")
    private val craft2 = Craft(2, "Bananas", 15.0, 97, "Yellow")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        inventoryDatabase = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        craftDao = inventoryDatabase.craftDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsCraftIntoDB() = runBlocking {
        addOneCraftToDb()
        val allCrafts = craftDao.getAllCrafts().first()
        assertEquals(allCrafts[0], craft1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllCrafts_returnsAllCraftsFromDB() = runBlocking {
        addTwoCraftsToDb()
        val allCrafts = craftDao.getAllCrafts().first()
        assertEquals(allCrafts[0], craft1)
        assertEquals(allCrafts[1], craft2)
    }


    @Test
    @Throws(Exception::class)
    fun daoGetCraft_returnsCraftFromDB() = runBlocking {
        addOneCraftToDb()
        val craft = craftDao.getCraft(1)
        assertEquals(craft.first(), craft1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteCrafts_deletesAllCraftsFromDB() = runBlocking {
        addTwoCraftsToDb()
        craftDao.delete(craft1)
        craftDao.delete(craft2)
        val allCrafts = craftDao.getAllCrafts().first()
        assertTrue(allCrafts.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateCrafts_updatesCraftsInDB() = runBlocking {
        addTwoCraftsToDb()
        craftDao.update(Craft(1, "Apples", 15.0, 25, "Green"))
        craftDao.update(Craft(2, "Bananas", 5.0, 50, "Light Yellow"))

        val allCrafts = craftDao.getAllCrafts().first()
        assertEquals(allCrafts[0], Craft(1, "Apples", 15.0, 25, "Green"))
        assertEquals(allCrafts[1], Craft(2, "Bananas", 5.0, 50, "Light Yellow"))
    }

    private suspend fun addOneCraftToDb() {
        craftDao.insert(craft1)
    }

    private suspend fun addTwoCraftsToDb() {
        craftDao.insert(craft1)
        craftDao.insert(craft2)
    }
}
