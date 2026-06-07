package com.example.guardia.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long

    @Update
    suspend fun updateExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE user_id = :userId ORDER BY created_at DESC")
    fun getExpensesByUserId(userId: Long): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE id = :expenseId LIMIT 1")
    suspend fun getExpenseById(expenseId: Long): Expense?

    @Query("SELECT SUM(amount) FROM expenses WHERE user_id = :userId")
    fun getTotalExpenses(userId: Long): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE user_id = :userId AND date LIKE :monthPattern")
    fun getMonthlyExpenses(userId: Long, monthPattern: String): Flow<Double?>

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenseById(expenseId: Long)
}