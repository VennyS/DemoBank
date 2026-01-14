package com.example.demobank.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cardNumber: String,
    val cardHolder: String,
    val balance: Double,
    val cardType: String, // VISA, MasterCard
    val color: String // blue, purple, green
)