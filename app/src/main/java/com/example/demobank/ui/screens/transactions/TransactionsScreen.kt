package com.example.demobank.ui.screens.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.demobank.ui.components.TransactionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionsViewModel = viewModel()
) {
    val transactions by viewModel.transactions.collectAsState()
    val cards by viewModel.cards.collectAsState()
    val selectedCardId by viewModel.selectedCardId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Все транзакции", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Фильтр по картам
            if (cards.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Кнопка "Все"
                    item {
                        FilterChip(
                            selected = selectedCardId == null,
                            onClick = { viewModel.selectCard(null) },
                            label = { Text("Все") }
                        )
                    }

                    // Кнопки для каждой карты
                    items(cards) { card ->
                        FilterChip(
                            selected = selectedCardId == card.id,
                            onClick = { viewModel.selectCard(card.id) },
                            label = {
                                Text("${card.cardType} ••${card.cardNumber.takeLast(4)}")
                            }
                        )
                    }
                }

                HorizontalDivider()
            }

            // Список транзакций
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (transactions.isEmpty()) {
                    item {
                        Text(
                            text = if (selectedCardId == null) "Нет транзакций" else "Нет транзакций по этой карте",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(transactions) { transaction ->
                        TransactionItem(
                            amount = transaction.amount,
                            type = transaction.type,
                            category = transaction.category,
                            description = transaction.description,
                            timestamp = transaction.timestamp
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
