package com.example.demobank.data.repository

import com.example.demobank.data.local.dao.CardDao
import com.example.demobank.data.local.dao.TransactionDao
import com.example.demobank.data.local.entity.Card
import com.example.demobank.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

class BankRepository(
    private val cardDao: CardDao,
    private val transactionDao: TransactionDao
) {
    // Cards
    fun getAllCards(): Flow<List<Card>> = cardDao.getAllCards()

    suspend fun getCardById(cardId: Long): Card? = cardDao.getCardById(cardId)

    suspend fun insertCard(card: Card): Long = cardDao.insertCard(card)

    suspend fun updateCard(card: Card) = cardDao.updateCard(card)

    suspend fun deleteCard(card: Card) = cardDao.deleteCard(card)

    suspend fun getCardCount(): Int = cardDao.getCardCount()

    // Transactions
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    fun getTransactionsByCard(cardId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByCard(cardId)

    suspend fun insertTransaction(transaction: Transaction): Long =
        transactionDao.insertTransaction(transaction)

    // Complex operations
    suspend fun performTransfer(fromCardId: Long, toCardId: Long, amount: Double) {
        // Получаем информацию о картах
        val fromCard = cardDao.getCardById(fromCardId)
        val toCard = cardDao.getCardById(toCardId)

        cardDao.updateBalance(fromCardId, -amount)
        cardDao.updateBalance(toCardId, amount)

        val timestamp = System.currentTimeMillis()

        // Формируем читаемые описания с информацией о картах
        val fromCardInfo = fromCard?.let { "${it.cardType} ••${it.cardNumber.takeLast(4)}" } ?: "карта"
        val toCardInfo = toCard?.let { "${it.cardType} ••${it.cardNumber.takeLast(4)}" } ?: "карта"

        transactionDao.insertTransaction(
            Transaction(
                cardId = fromCardId,
                amount = -amount,
                type = "TRANSFER",
                category = "Перевод",
                description = "На $toCardInfo",
                timestamp = timestamp
            )
        )

        transactionDao.insertTransaction(
            Transaction(
                cardId = toCardId,
                amount = amount,
                type = "TRANSFER",
                category = "Перевод",
                description = "С $fromCardInfo",
                timestamp = timestamp
            )
        )
    }
}
