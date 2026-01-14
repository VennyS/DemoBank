package com.example.demobank.ui.screens.transfer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.demobank.data.local.database.AppDatabase
import com.example.demobank.data.local.entity.Card
import com.example.demobank.data.local.entity.Transaction
import com.example.demobank.data.repository.BankRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransferViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val repository = BankRepository(database.cardDao(), database.transactionDao())

    val cards: StateFlow<List<Card>> = repository.getAllCards()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _transferStatus = MutableStateFlow<TransferStatus>(TransferStatus.Idle)
    val transferStatus: StateFlow<TransferStatus> = _transferStatus

    fun performTransfer(fromCardId: Long, toCardId: Long, amount: Double) {
        viewModelScope.launch {
            try {
                _transferStatus.value = TransferStatus.Loading

                val fromCard = repository.getCardById(fromCardId)
                if (fromCard == null) {
                    _transferStatus.value = TransferStatus.Error("Source card not found")
                    return@launch
                }

                if (fromCard.balance < amount) {
                    _transferStatus.value = TransferStatus.Error("Insufficient balance")
                    return@launch
                }

                if (fromCardId == toCardId) {
                    _transferStatus.value = TransferStatus.Error("Cannot transfer to same card")
                    return@launch
                }

                repository.performTransfer(fromCardId, toCardId, amount)
                _transferStatus.value = TransferStatus.Success
            } catch (e: Exception) {
                _transferStatus.value = TransferStatus.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun performExternalTransfer(fromCardId: Long, amount: Double, recipientPhone: String) {
        viewModelScope.launch {
            try {
                _transferStatus.value = TransferStatus.Loading

                val fromCard = repository.getCardById(fromCardId)
                if (fromCard == null) {
                    _transferStatus.value = TransferStatus.Error("Source card not found")
                    return@launch
                }

                if (fromCard.balance < amount) {
                    _transferStatus.value = TransferStatus.Error("Insufficient balance")
                    return@launch
                }

                // Генерируем случайный номер карты для получателя
                val randomCardNumber = "****${(1000..9999).random()}"

                // Списываем деньги с карты
                repository.updateCard(fromCard.copy(balance = fromCard.balance - amount))

                // Записываем транзакцию
                repository.insertTransaction(
                    Transaction(
                        cardId = fromCardId,
                        amount = -amount,
                        type = "TRANSFER",
                        category = "Перевод",
                        description = "по $recipientPhone ($randomCardNumber)",
                        timestamp = System.currentTimeMillis()
                    )
                )

                _transferStatus.value = TransferStatus.Success
            } catch (e: Exception) {
                _transferStatus.value = TransferStatus.Error(e.message ?: "Unknown error")
            }
        }
    }


    fun resetStatus() {
        _transferStatus.value = TransferStatus.Idle
    }
}

sealed class TransferStatus {
    object Idle : TransferStatus()
    object Loading : TransferStatus()
    object Success : TransferStatus()
    data class Error(val message: String) : TransferStatus()
}
