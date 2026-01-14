package com.example.demobank.ui.screens.cards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.demobank.data.local.database.AppDatabase
import com.example.demobank.data.local.entity.Card
import com.example.demobank.data.repository.BankRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class CardsManagementViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = BankRepository(database.cardDao(), database.transactionDao())

    val cards: StateFlow<List<Card>> = repository.getAllCards()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addNewCard(cardHolder: String, cardType: String, color: String, balance: Double) {
        viewModelScope.launch {
            val cardNumber = generateCardNumber()
            repository.insertCard(
                Card(
                    cardNumber = cardNumber,
                    cardHolder = cardHolder,
                    balance = balance,
                    cardType = cardType,
                    color = color
                )
            )
        }
    }

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            repository.deleteCard(card)
        }
    }

    private fun generateCardNumber(): String {
        val prefix = when (Random.nextInt(2)) {
            0 -> "4" // VISA
            else -> "5" // MasterCard
        }

        val remaining = (1..15).map { Random.nextInt(0, 10) }.joinToString("")
        return prefix + remaining
    }
}
