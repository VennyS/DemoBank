package com.example.demobank.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.demobank.data.local.dao.CardDao
import com.example.demobank.data.local.dao.TransactionDao
import com.example.demobank.data.local.entity.Card
import com.example.demobank.data.local.entity.Transaction

@Database(
    entities = [Card::class, Transaction::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bank_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
