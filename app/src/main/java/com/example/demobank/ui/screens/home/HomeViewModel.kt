package com.example.demobank.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.demobank.data.local.database.AppDatabase
import com.example.demobank.data.local.entity.Card
import com.example.demobank.data.local.entity.Transaction
import com.example.demobank.data.repository.BankRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = BankRepository(database.cardDao(), database.transactionDao())

    val cards: StateFlow<List<Card>> = repository.getAllCards()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<Transaction>> = repository.getAllTransactions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        initializeDemoData()
    }

    private fun initializeDemoData() {
        viewModelScope.launch {
            if (repository.getCardCount() == 0) {
                insertDemoData()
            }
        }
    }

    private fun insertDemoData() {
        viewModelScope.launch {
            // Демонстрационные карты
            repository.insertCard(
                Card(
                    cardNumber = "4532123456789012",
                    cardHolder = "Данил Панарин",
                    balance = 5420.50,
                    cardType = "VISA",
                    color = "blue"
                )
            )
            repository.insertCard(
                Card(
                    cardNumber = "5425233430109903",
                    cardHolder = "Данил Панарин",
                    balance = 2150.75,
                    cardType = "MasterCard",
                    color = "purple"
                )
            )

            // Демонстрационные транзакции
            val timestamp = System.currentTimeMillis()
            repository.insertTransaction(
                Transaction(
                    cardId = 1,
                    amount = -45.20,
                    type = "EXPENSE",
                    category = "Продукты",
                    description = "Супермаркет",
                    timestamp = timestamp - 3600000
                )
            )
            repository.insertTransaction(
                Transaction(
                    cardId = 1,
                    amount = 2500.0,
                    type = "INCOME",
                    category = "Зарплата",
                    description = "Месячная выплата",
                    timestamp = timestamp - 86400000
                )
            )
            repository.insertTransaction(
                Transaction(
                    cardId = 2,
                    amount = -120.50,
                    type = "EXPENSE",
                    category = "Покупки",
                    description = "Интернет‑магазин",
                    timestamp = timestamp - 7200000
                )
            )
        }
    }
}
