package com.example.guardia.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, Expense::class, SavingsGoal::class],
    version = 1,
    exportSchema = false // Keeps APK size smaller, but consider enabling schema export for version tracking in production
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun savingsGoalDao(): SavingsGoalDao

    companion object {
        @Volatile // Ensures visibility across threads — changes to INSTANCE are immediately visible to all threads
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Double-checked locking pattern — prevents multiple database instances being created

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, // Using app context prevents memory leaks from Activity/Fragment references
                    AppDatabase::class.java,
                    "guardia_database" // Descriptive database name
                )
                    .fallbackToDestructiveMigration() // Destroys existing database on version change

                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}