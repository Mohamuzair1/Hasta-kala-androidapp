package com.example.inventory.ui.billing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Craft
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.home.HomeViewModel
import com.example.inventory.ui.navigation.NavigationDestination

object QuickBillDestination : NavigationDestination {
    override val route = "quick_bill"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickBillScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    var selectedCraft by remember { mutableStateOf<Craft?>(null) }
    var selectedColor by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var itemSearchQuery by remember { mutableStateOf("") }
    var colorSearchQuery by remember { mutableStateOf("") }

    val availableCrafts = homeUiState.craftList.filter { it.quantity > 0 }
    val filteredCrafts = availableCrafts.filter {
        it.name.contains(itemSearchQuery.trim(), ignoreCase = true)
    }
    val colorOptions = buildList {
        if (!selectedCraft?.color.isNullOrBlank()) add(selectedCraft!!.color)
        addAll(availableCrafts.mapNotNull { it.color.takeIf { c -> c.isNotBlank() } })
    }.distinct()
    val filteredColors = colorOptions.filter {
        it.contains(colorSearchQuery.trim(), ignoreCase = true)
    }

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = "Quick Bill",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFF8F0), Color(0xFFF5E6D3), Color(0xFFE8D5B7))
                    )
                )
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Available Items",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7A3E1E)
            )
            OutlinedTextField(
                value = itemSearchQuery,
                onValueChange = { itemSearchQuery = it },
                singleLine = true,
                label = { Text("Search Item") },
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredCrafts) { craft ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedCraft = craft
                                selectedColor = craft.color
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedCraft?.id == craft.id) Color(0xFFFFECD6) else Color.White.copy(alpha = 0.95f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = craft.name, fontWeight = FontWeight.SemiBold)
                                Text(text = "Stock: ${craft.quantity}")
                            }
                            Text(text = "₹${String.format("%.2f", craft.price)}", color = Color(0xFFB4683A))
                        }
                    }
                }
            }

            Text(
                text = "Colors",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7A3E1E)
            )
            OutlinedTextField(
                value = colorSearchQuery,
                onValueChange = { colorSearchQuery = it },
                singleLine = true,
                label = { Text("Search Color") },
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredColors) { color ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedColor = color },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedColor == color) Color(0xFFFFECD6) else Color.White.copy(alpha = 0.95f)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = color,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val craft = selectedCraft
                    if (craft == null) {
                        message = "Please select an item."
                    } else {
                        viewModel.quickBillSale(craft.id, selectedColor) { result ->
                            message = result
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB4683A),
                    contentColor = Color.White
                )
            ) {
                Text("Save Sale")
            }

            if (message.isNotBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = message,
                    color = Color(0xFF5D2F0F)
                )
            }
        }
    }
}
