package com.example.demobank.data.local.dao

import androidx.room.*
import com.example.demobank.data.local.entity.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards")
    fun getAllCards(): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardById(cardId: Long): Card?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCard(card: Card): Long

    @Update
    suspend fun updateCard(card: Card)

    @Delete
    suspend fun deleteCard(card: Card)

    @Query("UPDATE cards SET balance = balance + :amount WHERE id = :cardId")
    suspend fun updateBalance(cardId: Long, amount: Double)

    @Query("SELECT COUNT(*) FROM cards")
    suspend fun getCardCount(): Int
}
