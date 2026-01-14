package com.example.demobank.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardId: Long,
    val amount: Double,
    val type: String, // INCOME, EXPENSE, TRANSFER
    val category: String,
    val description: String,
    val timestamp: Long
)
