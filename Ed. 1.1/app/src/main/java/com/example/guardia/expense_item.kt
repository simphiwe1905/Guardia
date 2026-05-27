package com.example.guardia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guardia.ExpenseAdapter
import kotlinx.coroutines.launch

/*
 Expense Item Activity (Expense History)
 This activity displays a scrollable list of all expenses recorded by the logged-in user.
 Features:
 -List view using RecyclerView for performance.
 -Summary of total expenses and count.
 -Option to delete individual expenses with a confirmation dialog.
 */
class expense_item : AppCompatActivity() {

    // UI component references
    lateinit var tvUsernameDisplay: TextView
    lateinit var tvTotalExpensesAmount: TextView
    lateinit var tvExpenseCount: TextView
    lateinit var recyclerViewExpenses: RecyclerView
    lateinit var btnReturnToDashboard: Button
    lateinit var btnLogout: Button
    lateinit var btnDashboardMenu: ImageButton
    lateinit var btnDashboardSettings: ImageButton

    // Adapter for the RecyclerView
    lateinit var expenseAdapter: ExpenseAdapter
    
    // User context state
    var userId: Long = -1
    var username: String = "User"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_item)

        // UI Customization: Hide the action bar
        supportActionBar?.hide()

        // Get user identity passed from previous activity
        userId = intent.getLongExtra("USER_ID", -1)
        username = intent.getStringExtra("USERNAME") ?: "User"

        // Security check
        if (userId == -1L) {
            val intent = Intent(this, login_page::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Initialize UI and load data
        initializeViews()
        setupRecyclerView()
        loadExpenses()
        setupClickListeners()
    }


     //Binds UI components and sets initial header text.
    fun initializeViews() {
        tvUsernameDisplay = findViewById(R.id.tvUsernameDisplay)
        tvTotalExpensesAmount = findViewById(R.id.tvTotalExpensesAmount)
        tvExpenseCount = findViewById(R.id.tvExpenseCount)
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses)
        btnReturnToDashboard = findViewById(R.id.btnReturnToDashboard)
        btnLogout = findViewById(R.id.btnLogout)
        btnDashboardMenu = findViewById(R.id.btnDashboardMenu)
        btnDashboardSettings = findViewById(R.id.btnDashboardSettings)

        tvUsernameDisplay.text = username
    }


     //Configures the RecyclerView with a layout manager and the custom ExpenseAdapter.
    fun setupRecyclerView() {
        // Initialize adapter with an empty list and a delete callback
        expenseAdapter = ExpenseAdapter(emptyList()) { expense ->
            showDeleteConfirmation(expense)
        }

        recyclerViewExpenses.apply {
            layoutManager = LinearLayoutManager(this@expense_item)
            adapter = expenseAdapter
            setHasFixedSize(true)
        }
    }


     //Observes the user's expenses from the database and updates the UI automatically.

    fun loadExpenses() {
        val application = application as GuardiaApplication

        lifecycleScope.launch {
            // Collect flow of expenses from repository
            application.repository.getExpensesByUserId(userId).collect { expenses ->
                expenseAdapter.updateExpenses(expenses)

                // Re-calculate summary statistics
                val totalAmount = expenses.sumOf { it.amount }
                tvTotalExpensesAmount.text = String.format("R %,.2f", totalAmount)
                tvExpenseCount.text = expenses.size.toString()

                if (expenses.isEmpty()) {
                    Toast.makeText(this@expense_item, "No expenses found. Add some!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


     //Displays a dialog to prevent accidental deletion of expense records.
    fun showDeleteConfirmation(expense: com.example.guardia.database.Expense) {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete '${expense.name}' (${String.format("R %,.2f", expense.amount)})?")
            .setPositiveButton("Delete") { _, _ ->
                deleteExpense(expense)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


     //Deletes the specified expense from the database via the Repository.
    fun deleteExpense(expense: com.example.guardia.database.Expense) {
        lifecycleScope.launch {
            try {
                val application = application as GuardiaApplication
                application.repository.deleteExpense(expense.id)
                Toast.makeText(this@expense_item, "Expense deleted", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@expense_item, "Error deleting expense: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


     //Configures click behavior for navigation and system buttons.
    fun setupClickListeners() {
        // Go back to Dashboard
        btnReturnToDashboard.setOnClickListener {
            val intent = Intent(this, dashboard::class.java)
            intent.putExtra("USER_ID", userId)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
            finish()
        }

        // System Logout
        btnLogout.setOnClickListener {
            val intent = Intent(this, login_page::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        // Notification toggle
        btnDashboardMenu.setOnClickListener {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
        }

        // Theme settings toggle
        btnDashboardSettings.setOnClickListener {
            Toast.makeText(this, "Theme settings", Toast.LENGTH_SHORT).show()
        }
    }
}