package com.example.guardia.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyGoalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: MonthlyGoal): Long

    @Update
    suspend fun updateGoal(goal: MonthlyGoal)

    @Delete
    suspend fun deleteGoal(goal: MonthlyGoal)

    @Query("SELECT * FROM monthly_goals WHERE user_id = :userId AND month_year = :monthYear LIMIT 1")
    suspend fun getGoalForMonth(userId: Long, monthYear: String): MonthlyGoal?

    @Query("SELECT * FROM monthly_goals WHERE user_id = :userId ORDER BY created_at DESC")
    fun getAllGoals(userId: Long): Flow<List<MonthlyGoal>>
}