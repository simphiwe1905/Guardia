package com.example.guardia.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/*
 Data Access Object (DAO) for the 'expenses' table.
 This interface defines all database operations related to user expenses.
 It supports basic CRUD operations and specific queries for financial reporting.
 */
@Dao
interface ExpenseDao {

    /*
     Records a new expense in the database.
     @return The row ID of the newly created expense record.
    */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense): Long


     //Updates an existing expense's details (e.g., amount, category, or date).
    @Update
    suspend fun updateExpense(expense: Expense)


     //Removes an expense record from the database.
    @Delete
    suspend fun deleteExpense(expense: Expense)

    /*
     Retrieves all expenses associated with a specific user, sorted by date (newest first).
     @param userId The ID of the owner of the expenses.
     @return A Flow emitting a list of expenses whenever the data changes.
     */
    @Query("SELECT * FROM expenses WHERE user_id = :userId ORDER BY created_at DESC")
    fun getExpensesByUserId(userId: Long): Flow<List<Expense>>

    /*
     Fetches a single expense record by its unique ID.
     */
    @Query("SELECT * FROM expenses WHERE id = :expenseId LIMIT 1")
    suspend fun getExpenseById(expenseId: Long): Expense?

    /*
     Filters expenses for a user based on a specific category.
     */
    @Query("SELECT * FROM expenses WHERE user_id = :userId AND category = :category")
    fun getExpensesByCategory(userId: Long, category: String): Flow<List<Expense>>

    /*
     Calculates the total sum of all expenses recorded by a specific user.
     @return A Flow emitting the sum as a Double.
     */
    @Query("SELECT SUM(amount) FROM expenses WHERE user_id = :userId")
    fun getTotalExpenses(userId: Long): Flow<Double?>

    /*
     Calculates the total sum of expenses for a user within a specific month.
     @param month A pattern used to match the month (e.g., "%05/2024%").
     */
    @Query("SELECT SUM(amount) FROM expenses WHERE user_id = :userId AND date LIKE :month")
    fun getMonthlyExpenses(userId: Long, month: String): Flow<Double?>


     //Deletes an expense record using its primary key ID
    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenseById(expenseId: Long)


     //Identifies the single largest expense recorded by a user.
    @Query("SELECT * FROM expenses WHERE user_id = :userId ORDER BY amount DESC LIMIT 1")
    suspend fun getLargestExpense(userId: Long): Expense?
}
