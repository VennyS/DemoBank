package com.example.demobank.ui.screens.home

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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.demobank.ui.components.BankCard
import com.example.demobank.ui.components.TransactionItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val cards by viewModel.cards.collectAsState()
    val transactions by viewModel.transactions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои карты", fontWeight = FontWeight.Bold) }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Cards carousel
            item {
                if (cards.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(cards) { card ->
                            BankCard(
                                cardNumber = card.cardNumber,
                                cardHolder = card.cardHolder,
                                balance = card.balance,
                                cardType = card.cardType,
                                color = card.color,
                                modifier = Modifier.width(340.dp)
                            )
                        }
                    }
                } else {
                    Text("Нет карт")
                }
            }

            // Recent Transactions
            item {
                Text(
                    text = "Недавние операции",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(transactions.take(5)) { transaction ->
                TransactionItem(
                    amount = transaction.amount,
                    type = transaction.type,
                    category = transaction.category,
                    description = transaction.description,
                    timestamp = transaction.timestamp
                )
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
