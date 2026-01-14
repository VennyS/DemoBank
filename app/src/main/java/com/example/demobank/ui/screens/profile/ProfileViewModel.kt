package com.example.demobank.ui.screens.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.demobank.data.local.database.AppDatabase
import com.example.demobank.data.local.entity.Card
import com.example.demobank.data.repository.BankRepository
import kotlinx.coroutines.launch
import kotlin.random.Random

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = BankRepository(database.cardDao(), database.transactionDao())

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

    private fun generateCardNumber(): String {
        val prefix = when (Random.nextInt(2)) {
            0 -> "4" // VISA
            else -> "5" // MasterCard
        }

        val remaining = (1..15).map { Random.nextInt(0, 10) }.joinToString("")
        return prefix + remaining
    }
}
