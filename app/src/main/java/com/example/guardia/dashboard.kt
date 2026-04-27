package com.example.guardia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/*
 * Dashboard Activity
 This is the main hub of the application after the user logs in.
 It displays a summary of the user's financial status, including:
 -Total balance (sum of all expenses)
 -Monthly expenses (for the current month)
 -Savings progress (current vs. target)
 It provides navigation to all main features:
 -Adding new expenses
 -Viewing expense history
 -Managing savings goals
 */
class dashboard : AppCompatActivity() {

    // UI component references
    lateinit var tvUsernameDisplay: TextView
    lateinit var tvTotalBalanceValue: TextView
    lateinit var tvExpensesValue: TextView
    lateinit var tvSavingsValue: TextView
    lateinit var btnAddExpense: Button
    lateinit var btnViewExpenses: Button
    lateinit var btnGoals: Button
    lateinit var btnAnalysis: Button
    lateinit var btnAchievements: Button
    lateinit var btnLogout: Button
    lateinit var btnDashboardMenu: ImageButton
    lateinit var btnDashboardSettings: ImageButton

    // State variables
    var userId: Long = -1
    var username: String = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // UI Customization: Hide the action bar for a cleaner look
        supportActionBar?.hide()

        // UI Customization: Make the app draw behind system bars for immersive feel
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Immersive Mode: Hide status bar and navigation bar
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Security Check: Get user data from intent and redirect to login if missing
        userId = intent.getLongExtra("USER_ID", -1)
        username = intent.getStringExtra("USERNAME") ?: "User"

        if (userId == -1L) {
            val intent = Intent(this, login_page::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Initialize UI and load data
        initializeViews()
        displayUserInfo()
        loadFinancialData()
        setupClickListeners()
    }


     //Binds UI components from the layout to the activity variables.
    fun initializeViews() {
        tvUsernameDisplay = findViewById(R.id.tvUsernameDisplay)
        tvTotalBalanceValue = findViewById(R.id.tvTotalBalanceValue)
        tvExpensesValue = findViewById(R.id.tvExpensesValue)
        tvSavingsValue = findViewById(R.id.tvSavingsValue)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        btnViewExpenses = findViewById(R.id.btnViewExpenses)
        btnGoals = findViewById(R.id.btnGoals)
        btnAnalysis = findViewById(R.id.btnAnalysis)
        btnAchievements = findViewById(R.id.btnAchievements)
        btnLogout = findViewById(R.id.btnLogout)
        btnDashboardMenu = findViewById(R.id.btnDashboardMenu)
        btnDashboardSettings = findViewById(R.id.btnDashboardSettings)
    }


     //Displays the logged-in user's name.
    fun displayUserInfo() {
        tvUsernameDisplay.text = username
    }

    /*
     Fetches financial data from the database via the Repository and updates the UI.
     Uses coroutines and Flow to react to database changes in real-time.
     */
    fun loadFinancialData() {
        val application = application as GuardiaApplication

        // Format for identifying the current month's expenses
        val dateFormat = SimpleDateFormat("MM/yyyy", Locale.getDefault())
        val currentMonth = dateFormat.format(Date())

        // Fetch Total Expenses
        lifecycleScope.launch {
            application.repository.getTotalExpenses(userId).collect { totalExpenses ->
                val expenses = totalExpenses ?: 0.0
                tvTotalBalanceValue.text = String.format("R %,.2f", expenses)
            }
        }

        // Fetch Current Month's Expenses
        lifecycleScope.launch {
            application.repository.getMonthlyExpenses(userId, "%$currentMonth%").collect { monthlyExpenses ->
                val monthly = monthlyExpenses ?: 0.0
                tvExpensesValue.text = String.format("R %,.2f", monthly)
            }
        }

        // Fetch Savings Goals Progress
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


     //Configures the behavior for all clickable UI elements.
    fun setupClickListeners() {
        // Navigate to Add Expense page
        btnAddExpense.setOnClickListener {
            val intent = Intent(this, expense_page::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        // Navigate to View Expenses history
        btnViewExpenses.setOnClickListener {
            val intent = Intent(this, expense_item::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        // Navigate to Savings Goals page
        btnGoals.setOnClickListener {
            val intent = Intent(this, goals_page::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
        }

        // Placeholder for Analysis feature
        btnAnalysis.setOnClickListener {
            Toast.makeText(this, "Analysis feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Placeholder for Achievements feature
        btnAchievements.setOnClickListener {
            Toast.makeText(this, "Achievements feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Logout and return to login screen
        btnLogout.setOnClickListener {
            val intent = Intent(this, login_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Notification shortcut
        btnDashboardMenu.setOnClickListener {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
        }

        // Settings/Theme shortcut
        btnDashboardSettings.setOnClickListener {
            Toast.makeText(this, "Theme settings", Toast.LENGTH_SHORT).show()
        }
    }
}