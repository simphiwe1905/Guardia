package com.example.guardia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class dashboard : AppCompatActivity() {

    lateinit var tvUsernameDisplay: TextView
    lateinit var tvTotalBalanceValue: TextView
    lateinit var tvExpensesValue: TextView
    lateinit var tvSavingsValue: TextView
    lateinit var tvMonthlyGoal: TextView
    lateinit var btnAddExpense: Button
    lateinit var btnViewExpenses: Button
    lateinit var btnGoals: Button
    lateinit var btnAnalysis: Button
    lateinit var btnAchievements: Button
    lateinit var btnLogout: Button
    lateinit var btnDashboardMenu: ImageButton
    lateinit var btnDashboardSettings: ImageButton

    var userId: Long = -1
    var username: String = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        supportActionBar?.hide()

        userId = intent.getLongExtra("USER_ID", -1)
        username = intent.getStringExtra("USERNAME") ?: "User"

        if (userId == -1L) {
            val intent = Intent(this, login_page::class.java)
            startActivity(intent)
            finish()
            return
        }

        initializeViews()
        displayUserInfo()
        loadFinancialData()
        loadMonthlyGoal()
        setupClickListeners()
    }

    fun initializeViews() {
        tvUsernameDisplay = findViewById(R.id.tvUsernameDisplay)
        tvTotalBalanceValue = findViewById(R.id.tvTotalBalanceValue)
        tvExpensesValue = findViewById(R.id.tvExpensesValue)
        tvSavingsValue = findViewById(R.id.tvSavingsValue)
        tvMonthlyGoal = findViewById(R.id.tvMonthlyGoal)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        btnViewExpenses = findViewById(R.id.btnViewExpenses)
        btnGoals = findViewById(R.id.btnGoals)
        btnAnalysis = findViewById(R.id.btnAnalysis)
        btnAchievements = findViewById(R.id.btnAchievements)
        btnLogout = findViewById(R.id.btnLogout)
        btnDashboardMenu = findViewById(R.id.btnDashboardMenu)
        btnDashboardSettings = findViewById(R.id.btnDashboardSettings)
    }

    fun displayUserInfo() {
        tvUsernameDisplay.text = username
    }

    fun loadFinancialData() {
        val application = application as GuardiaApplication

        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        val currentMonth = dateFormat.format(Date())
        val monthPattern = "%/$currentMonth"

        lifecycleScope.launch {
            application.repository.getTotalExpenses(userId).collect { totalExpenses ->
                val expenses = totalExpenses ?: 0.0
                tvTotalBalanceValue.text = String.format("R %,.2f", expenses)
            }
        }

        lifecycleScope.launch {
            application.repository.getMonthlyExpenses(userId, monthPattern).collect { monthlyExpenses ->
                val monthly = monthlyExpenses ?: 0.0
                tvExpensesValue.text = String.format("R %,.2f", monthly)
            }
        }

        lifecycleScope.launch {
            application.repository.getTotalCurrentAmount(userId).collect { currentAmount ->
                application.repository.getTotalTargetAmount(userId).collect { targetAmount ->
                    val current = currentAmount ?: 0.0
                    val target = targetAmount ?: 0.0
                    tvSavingsValue.text = String.format("R %,.0f / R %,.0f", current, target)
                }
            }
        }
    }

    fun loadMonthlyGoal() {
        val application = application as GuardiaApplication

        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        val currentMonth = dateFormat.format(Date())

        lifecycleScope.launch {
            val goal = application.repository.getMonthlyGoal(userId, currentMonth)

            if (goal != null) {
                val monthlyExpenses = application.repository.getMonthlyExpenses(userId, "%/$currentMonth")
                monthlyExpenses.collect { expenses ->
                    val spent = expenses ?: 0.0
                    val status = when {
                        spent < goal.minAmount -> "Below minimum goal"
                        spent > goal.maxAmount -> "Over maximum limit!"
                        else -> "Within your goal range"
                    }

                    tvMonthlyGoal.text = String.format(
                        "Min: R %,.0f | Max: R %,.0f\nSpent: R %,.2f\n%s",
                        goal.minAmount,
                        goal.maxAmount,
                        spent,
                        status
                    )
                }
            } else {
                tvMonthlyGoal.text = "No monthly goal set\nTap Goals to set one"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadMonthlyGoal()
    }

    fun setupClickListeners() {
        btnAddExpense.setOnClickListener {
            val intent = Intent(this, expense_page::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        btnViewExpenses.setOnClickListener {
            val intent = Intent(this, expense_item::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }

        btnGoals.setOnClickListener {
            val intent = Intent(this, goals_page::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        btnAnalysis.setOnClickListener {
            Toast.makeText(this, "Analysis feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        btnAchievements.setOnClickListener {
            Toast.makeText(this, "Achievements feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, login_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnDashboardMenu.setOnClickListener {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
        }

        btnDashboardSettings.setOnClickListener {
            Toast.makeText(this, "Theme settings", Toast.LENGTH_SHORT).show()
        }
    }
}