package com.example.guardia.database

import kotlinx.coroutines.flow.Flow

class GuardiaRepository(private val database: AppDatabase) {

    val userDao = database.userDao()
    val expenseDao = database.expenseDao()
    val savingsGoalDao = database.savingsGoalDao()
    val monthlyGoalDao = database.monthlyGoalDao()

    // User operations
    suspend fun registerUser(username: String, email: String, password: String): Long {
        val user = User(
            username = username,
            email = email,
            password = password
        )
        return userDao.insertUser(user)
    }

    suspend fun loginUser(username: String, password: String): User? {
        return userDao.loginUser(username, password)
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    suspend fun isUsernameTaken(username: String): Boolean {
        return userDao.getUserByUsername(username) != null
    }

    suspend fun isEmailTaken(email: String): Boolean {
        return userDao.getUserByEmail(email) != null
    }

    // Expense operations
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

    suspend fun addExpenseWithImage(
        userId: Long,
        name: String,
        amount: Double,
        category: String,
        date: String,
        description: String,
        imagePath: String
    ): Long {
        val expense = Expense(
            userId = userId,
            name = name,
            amount = amount,
            category = category,
            date = date,
            description = description,
            imagePath = imagePath
        )
        return expenseDao.insertExpense(expense)
    }

    fun getExpensesByUserId(userId: Long): Flow<List<Expense>> {
        return expenseDao.getExpensesByUserId(userId)
    }

    fun getTotalExpenses(userId: Long): Flow<Double?> {
        return expenseDao.getTotalExpenses(userId)
    }

    fun getMonthlyExpenses(userId: Long, month: String): Flow<Double?> {
        return expenseDao.getMonthlyExpenses(userId, month)
    }

    suspend fun deleteExpense(expenseId: Long) {
        expenseDao.deleteExpenseById(expenseId)
    }

    // Savings Goal operations
    suspend fun addSavingsGoal(
        userId: Long,
        name: String,
        targetAmount: Double
    ): Long {
        val goal = SavingsGoal(
            userId = userId,
            name = name,
            targetAmount = targetAmount,
            currentAmount = 0.0
        )
        return savingsGoalDao.insertGoal(goal)
    }

    fun getSavingsGoals(userId: Long): Flow<List<SavingsGoal>> {
        return savingsGoalDao.getGoalsByUserId(userId)
    }

    fun getTotalTargetAmount(userId: Long): Flow<Double?> {
        return savingsGoalDao.getTotalTargetAmount(userId)
    }

    fun getTotalCurrentAmount(userId: Long): Flow<Double?> {
        return savingsGoalDao.getTotalCurrentAmount(userId)
    }

    suspend fun updateSavingsGoal(goal: SavingsGoal) {
        savingsGoalDao.updateGoal(goal)
    }

    // Monthly Goal operations
    suspend fun setMonthlyGoal(userId: Long, monthYear: String, minAmount: Double, maxAmount: Double): Long {
        val existingGoal = monthlyGoalDao.getGoalForMonth(userId, monthYear)
        return if (existingGoal != null) {
            val updatedGoal = existingGoal.copy(minAmount = minAmount, maxAmount = maxAmount)
            monthlyGoalDao.updateGoal(updatedGoal)
            existingGoal.id
        } else {
            val goal = MonthlyGoal(
                userId = userId,
                monthYear = monthYear,
                minAmount = minAmount,
                maxAmount = maxAmount
            )
            monthlyGoalDao.insertGoal(goal)
        }
    }

    suspend fun getMonthlyGoal(userId: Long, monthYear: String): MonthlyGoal? {
        return monthlyGoalDao.getGoalForMonth(userId, monthYear)
    }

    fun getAllMonthlyGoals(userId: Long): Flow<List<MonthlyGoal>> {
        return monthlyGoalDao.getAllGoals(userId)
    }
}