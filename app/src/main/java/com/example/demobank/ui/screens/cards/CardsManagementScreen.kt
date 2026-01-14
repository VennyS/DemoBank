package com.example.demobank.ui.screens.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.demobank.ui.components.BankCard
import com.example.demobank.ui.screens.profile.AddCardDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsManagementScreen(
    navController: NavController,
    viewModel: CardsManagementViewModel = viewModel()
) {
    val cards by viewModel.cards.collectAsState()
    var showAddCardDialog by remember { mutableStateOf(false) }

    if (showAddCardDialog) {
        AddCardDialog(
            onDismiss = { showAddCardDialog = false },
            onCardAdded = { holder, type, color, balance ->
                viewModel.addNewCard(holder, type, color, balance)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Управление картами", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCardDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить карту")
            }
        }
    ) { paddingValues ->
        if (cards.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Нет карт. Добавьте новую карту!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(cards) { card ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BankCard(
                            cardNumber = card.cardNumber,
                            cardHolder = card.cardHolder,
                            balance = card.balance,
                            cardType = card.cardType,
                            color = card.color
                        )

                        OutlinedButton(
                            onClick = { viewModel.deleteCard(card) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Удалить карту")
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
