package com.example.demobank.data.local.dao

import androidx.room.*
import com.example.demobank.data.local.entity.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE cardId = :cardId ORDER BY timestamp DESC")
    fun getTransactionsByCard(cardId: Long): Flow<List<Transaction>>

    @Insert
    suspend fun insertTransaction(transaction: Transaction): Long
}
