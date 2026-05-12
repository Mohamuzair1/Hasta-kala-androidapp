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

package com.example.inventory.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inventory.ui.craft.CraftDetailsDestination
import com.example.inventory.ui.craft.CraftDetailsScreen
import com.example.inventory.ui.craft.CraftEditDestination
import com.example.inventory.ui.craft.CraftEditScreen
import com.example.inventory.ui.craft.CraftEntryDestination
import com.example.inventory.ui.craft.CraftEntryScreen
import com.example.inventory.ui.billing.QuickBillDestination
import com.example.inventory.ui.billing.QuickBillScreen
import com.example.inventory.ui.home.HomeDestination
import com.example.inventory.ui.home.HomeScreen
import com.example.inventory.ui.login.LoginDestination
import com.example.inventory.ui.login.LoginScreen
import com.example.inventory.ui.dashboard.BestSellerDashboardDestination
import com.example.inventory.ui.dashboard.BestSellerDashboardScreen
import com.example.inventory.ui.dashboard.MostSellingDashboardDestination
import com.example.inventory.ui.dashboard.MostSellingDashboardScreen

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun InventoryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        composable(route = LoginDestination.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(HomeDestination.route) {
                        popUpTo(LoginDestination.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToCraftEntry = {
                    navController.navigate(CraftEntryDestination.route)
                },
                navigateToCraftUpdate = {
                    navController.navigate("${CraftDetailsDestination.route}/${it}")
                },
                navigateToDashboard = {
                    navController.navigate(BestSellerDashboardDestination.route)
                },
                navigateToMostSelling = {
                    navController.navigate(MostSellingDashboardDestination.route)
                },
                navigateToQuickBill = {
                    navController.navigate(QuickBillDestination.route)
                },
                onLogout = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(HomeDestination.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(route = CraftEntryDestination.route) {
            CraftEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(
            route = CraftDetailsDestination.routeWithArgs,
            arguments = listOf(
                navArgument(CraftDetailsDestination.craftIdArg) {
                    type = NavType.IntType
                }
            )
        ) {
            CraftDetailsScreen(
                navigateToEditCraft = {
                    navController.navigate("${CraftEditDestination.route}/$it")
                },
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = CraftEditDestination.routeWithArgs,
            arguments = listOf(
                navArgument(CraftEditDestination.craftIdArg) {
                    type = NavType.IntType
                }
            )
        ) {
            CraftEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

        composable(route = BestSellerDashboardDestination.route) {
            BestSellerDashboardScreen(
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(route = MostSellingDashboardDestination.route) {
            MostSellingDashboardScreen(
                navigateBack = { navController.navigateUp() }
            )
        }

        composable(route = QuickBillDestination.route) {
            QuickBillScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}