package com.example.guardia.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/*
 Data Access Object (DAO) for the 'savings_goals' table.
 This interface defines the database operations for managing user savings goals.
 It allows users to track their progress towards financial targets.
 */
@Dao
interface SavingsGoalDao {

    /*
     Inserts a new savings goal into the database.
     @return The row ID of the newly created goal.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: SavingsGoal): Long


     //Updates an existing savings goal (e.g., updating the current saved amount).

    @Update
    suspend fun updateGoal(goal: SavingsGoal)


     //Deletes a savings goal record from the database.
    @Delete
    suspend fun deleteGoal(goal: SavingsGoal)

    /*
     Retrieves all savings goals for a specific user.
     @return A Flow that emits a list of goals whenever the table is updated.
     */
    @Query("SELECT * FROM savings_goals WHERE user_id = :userId")
    fun getGoalsByUserId(userId: Long): Flow<List<SavingsGoal>>


     //Fetches a specific savings goal by its unique ID.
    @Query("SELECT * FROM savings_goals WHERE id = :goalId LIMIT 1")
    suspend fun getGoalById(goalId: Long): SavingsGoal?

    /*
     Calculates the total target amount across all savings goals for a user.
     Useful for displaying aggregate progress on the dashboard.
     */
    @Query("SELECT SUM(target_amount) FROM savings_goals WHERE user_id = :userId")
    fun getTotalTargetAmount(userId: Long): Flow<Double?>


     //Calculates the total current amount saved across all goals for a user.

    @Query("SELECT SUM(current_amount) FROM savings_goals WHERE user_id = :userId")
    fun getTotalCurrentAmount(userId: Long): Flow<Double?>


     //Removes a savings goal from the database using its ID.
    @Query("DELETE FROM savings_goals WHERE id = :goalId")
    suspend fun deleteGoalById(goalId: Long)
}
