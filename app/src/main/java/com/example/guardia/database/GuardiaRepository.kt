package com.example.guardia.database

import kotlinx.coroutines.flow.Flow

/*
 GuardiaRepository
 This class serves as a clean API for the rest of the application to access data.
 It abstracts the source of data (in this case, the Room database) from the UI layer.
 All database operations are coordinated through this repository.
 */
class GuardiaRepository(private val database: AppDatabase) {

    // DAOs (Data Access Objects) extracted from the database instance
    val userDao = database.userDao()
    val expenseDao = database.expenseDao()
    val savingsGoalDao = database.savingsGoalDao()

    // --- User Operations ---
     //Registers a new user by creating a User entity and inserting it into the database.
    suspend fun registerUser(username: String, email: String, password: String): Long {
        val user = User(
            username = username,
            email = email,
            password = password
        )
        return userDao.insertUser(user)
    }


     //Attempts to log in a user by verifying their credentials.
    suspend fun loginUser(username: String, password: String): User? {
        return userDao.loginUser(username, password)
    }


     //Fetches a user's profile information by their unique ID.
    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }


     //Checks if a specific username is already taken in the database.
    suspend fun isUsernameTaken(username: String): Boolean {
        return userDao.getUserByUsername(username) != null
    }


     //Checks if a specific email address is already registered.
    suspend fun isEmailTaken(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }

    // --- Expense Operations ---
     //Adds a new expense record for a specific user.
    suspend fun addExpense(
        userId: Long,
        name: String,
        amount: Double,
        category: String,
        date: String,
        description: String
    ): Long {
        val expense = Expense(
            userId = userId,
            name = name,
            amount = amount,
            category = category,
            date = date,
            description = description
        )
        return expenseDao.insertExpense(expense)
    }


     //Provides a stream of all expenses for a user.
    fun getExpensesByUserId(userId: Long): Flow<List<Expense>> {
        return expenseDao.getExpensesByUserId(userId)
    }


     //Provides a stream of the total sum of all expenses for a user.
    fun getTotalExpenses(userId: Long): Flow<Double?> {
        return expenseDao.getTotalExpenses(userId)
    }


     //Provides a stream of expenses for a user filtered by a specific month.
    fun getMonthlyExpenses(userId: Long, month: String): Flow<Double?> {
        return expenseDao.getMonthlyExpenses(userId, month)
    }


     //Deletes a specific expense record.
    suspend fun deleteExpense(expenseId: Long) {
        expenseDao.deleteExpenseById(expenseId)
    }

    // --- Savings Goal Operations ---

    /*
     Adds a new savings goal for a user.
     Note: This implementation matches the existing DAO requirements.
     */
    suspend fun addSavingsGoal(
        userId: Long,
        name: String,
        targetAmount: Double,
        currentAmount: Double
    ): Long {
        val goal = SavingsGoal(
            userId = userId,
            name = name,
            targetAmount = targetAmount,
            currentAmount = currentAmount
        )
        return savingsGoalDao.insertGoal(goal)
    }


     //Provides a stream of all savings goals for a user.
    fun getSavingsGoals(userId: Long): Flow<List<SavingsGoal>> {
        return savingsGoalDao.getGoalsByUserId(userId)
    }


     //Provides a stream of the combined target amount of all goals for a user.
    fun getTotalTargetAmount(userId: Long): Flow<Double?> {
        return savingsGoalDao.getTotalTargetAmount(userId)
    }


     //Provides a stream of the combined current saved amount of all goals for a user.
    fun getTotalCurrentAmount(userId: Long): Flow<Double?> {
        return savingsGoalDao.getTotalCurrentAmount(userId)
    }


     //Updates an existing savings goal record.
    suspend fun updateGoal(goal: SavingsGoal) {
        savingsGoalDao.updateGoal(goal)
    }
}
