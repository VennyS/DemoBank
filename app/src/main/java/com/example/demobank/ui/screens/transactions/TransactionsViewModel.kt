package com.example.demobank.ui.screens.transactions

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.demobank.data.local.database.AppDatabase
import com.example.demobank.data.local.entity.Card
import com.example.demobank.data.local.entity.Transaction
import com.example.demobank.data.repository.BankRepository
import kotlinx.coroutines.flow.*

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = BankRepository(database.cardDao(), database.transactionDao())

    val cards: StateFlow<List<Card>> = repository.getAllCards()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedCardId = MutableStateFlow<Long?>(null)
    val selectedCardId: StateFlow<Long?> = _selectedCardId

    val transactions: StateFlow<List<Transaction>> = combine(
        repository.getAllTransactions(),
        _selectedCardId
    ) { allTransactions, cardId ->
        if (cardId == null) {
            allTransactions
        } else {
            allTransactions.filter { it.cardId == cardId }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectCard(cardId: Long?) {
        _selectedCardId.value = cardId
    }
}
