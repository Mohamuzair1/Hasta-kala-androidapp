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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.inventory.data.Craft
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.craft.formatedPrice
import com.example.inventory.ui.navigation.NavigationDestination
import com.example.inventory.ui.theme.InventoryTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToCraftEntry: () -> Unit,
    navigateToCraftUpdate: (Int) -> Unit,
    navigateToDashboard: () -> Unit,
    navigateToMostSelling: () -> Unit,
    navigateToQuickBill: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToCraftEntry,
                shape = MaterialTheme.shapes.medium,
                containerColor = Color(0xFFD4A574),
                contentColor = Color.White,
                modifier = Modifier
                    .padding(
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateEndPadding(LocalLayoutDirection.current)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.item_entry_title)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            craftList = homeUiState.craftList,
            onCraftClick = navigateToCraftUpdate,
            onDashboardClick = navigateToDashboard,
            onMostSellingClick = navigateToMostSelling,
            onQuickBillClick = navigateToQuickBill,
            onLogoutClick = onLogout,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
        )
    }
}

@Composable
private fun HomeBody(
    craftList: List<Craft>,
    onCraftClick: (Int) -> Unit,
    onDashboardClick: () -> Unit,
    onMostSellingClick: () -> Unit,
    onQuickBillClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Traditional Indian handicraft background
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

        // Semi-transparent overlay for text readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x11D4A574)) // Very subtle terracotta overlay
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Elegant header with traditional Indian design elements
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFC88A53),
                                Color(0xFFB4683A),
                                Color(0xFF7A3E1E)
                            )
                        )
                    )
                    .padding(vertical = 28.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "✦", color = Color(0xFFFFF2DE), fontSize = 16.sp)
                    Text(text = "✦", color = Color(0xFFFFE1BA), fontSize = 14.sp)
                    Text(text = "✦", color = Color(0xFFFFF2DE), fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(HomeDestination.titleRes),
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "हस्तकला शॉप",
                    fontSize = 18.sp,
                    color = Color(0xFFFFE8CC),
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Handcrafted Treasures",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFFF0DA),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Discover the Art of Handmade Crafts",
                    fontSize = 14.sp,
                    color = Color(0xFFE6D7C3),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.height(12.dp))
                DecorativeDivider()

                // Dashboard button
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onDashboardClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFF8DC).copy(alpha = 0.9f),
                            contentColor = Color(0xFF8B4513)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "📊 View Sales Dashboard",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                    Button(
                        onClick = onMostSellingClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFF8DC).copy(alpha = 0.9f),
                            contentColor = Color(0xFF8B4513)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "🏆 Most Selling Crafts",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onQuickBillClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD4A574),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Quick Bill",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onLogoutClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B4513).copy(alpha = 0.92f),
                        contentColor = Color(0xFFFFF2DE)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Logout",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }

            if (craftList.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "🪔",
                        fontSize = 48.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No crafts in the shop yet",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFF8B4513),
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add your first handmade craft to begin",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFFA0826D)
                    )
                }
            } else {
                CraftList(
                    craftList = craftList,
                    onCraftClick = { onCraftClick(it.id) },
                    contentPadding = contentPadding,
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}

@Composable
private fun CraftList(
    craftList: List<Craft>,
    onCraftClick: (Craft) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = craftList, key = { it.id }) { craft ->
            CraftItem(craft = craft,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onCraftClick(craft) })
        }
    }
}

@Composable
private fun CraftItem(
    craft: Craft, modifier: Modifier = Modifier
) {
    // Generate color based on craft color name with Indian-inspired colors
    val accentColor = getColorForCraft(craft.color)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(148.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Color(0xFFE8D5B7).copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decorative left border with craft color
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(accentColor, accentColor.copy(alpha = 0.6f))
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            // Left side: Craft details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "🪔",
                            fontSize = 16.sp
                        )
                        Text(
                            text = craft.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C2C2C),
                            fontSize = 19.sp,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    if (craft.color.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(12.dp)
                                    .height(12.dp)
                                    .background(accentColor, shape = RoundedCornerShape(6.dp))
                            )
                            Text(
                                text = craft.color,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF666666),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "📦",
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${craft.quantity} in stock",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color(0xFF8B4513),
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }
                if (craft.quantity <= 2) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Stock Alert: Only ${craft.quantity} left - Time to make more.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB00020),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Right side: Price and action chip
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
            ) {
                Text(
                    text = craft.formatedPrice(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD4A574),
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    accentColor.copy(alpha = 0.2f),
                                    accentColor.copy(alpha = 0.1f)
                                )
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "View",
                        style = MaterialTheme.typography.labelMedium,
                        color = accentColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DecorativeDivider() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(34.dp)
                .height(1.dp)
                .background(Color(0xFFFFE6C7))
        )
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(Color(0xFFFFEACD), shape = RoundedCornerShape(3.dp))
        )
        Box(
            modifier = Modifier
                .width(34.dp)
                .height(1.dp)
                .background(Color(0xFFFFE6C7))
        )
    }
}

private fun getColorForCraft(colorName: String): Color {
    return when (colorName.lowercase()) {
        "red" -> Color(0xFFDC143C)      // Crimson
        "blue" -> Color(0xFF4169E1)     // Royal Blue
        "green" -> Color(0xFF228B22)    // Forest Green
        "yellow" -> Color(0xFFFFD700)   // Gold
        "purple" -> Color(0xFF8B008B)   // Dark Magenta
        "pink" -> Color(0xFFFF69B4)     // Hot Pink
        "orange" -> Color(0xFFFF8C00)   // Dark Orange
        "black" -> Color(0xFF2F2F2F)    // Dark Gray
        "white" -> Color(0xFFF5F5F5)    // White Smoke
        "brown" -> Color(0xFF8B4513)    // Saddle Brown
        "maroon" -> Color(0xFF800000)   // Maroon
        "beige" -> Color(0xFFF5F5DC)    // Beige
        "terracotta" -> Color(0xFFD2691E) // Chocolate
        "sandalwood" -> Color(0xFFC4A484) // Sandalwood
        else -> Color(0xFFD4A574)       // Default terracotta
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    InventoryTheme {
        HomeBody(listOf(
            Craft(1, "Handwoven Basket", 150.0, 5, "Brown"),
            Craft(2, "Embroidered Cushion", 300.0, 8, "Maroon"),
            Craft(3, "Clay Pottery", 200.0, 12, "Terracotta")
        ), onCraftClick = {}, onDashboardClick = {}, onMostSellingClick = {}, onQuickBillClick = {}, onLogoutClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    InventoryTheme {
        HomeBody(listOf(), onCraftClick = {}, onDashboardClick = {}, onMostSellingClick = {}, onQuickBillClick = {}, onLogoutClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun CraftItemPreview() {
    InventoryTheme {
        CraftItem(
            Craft(1, "Handwoven Basket", 150.0, 5, "Brown"),
        )
    }
}
