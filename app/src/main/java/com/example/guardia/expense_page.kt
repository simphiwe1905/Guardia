package com.example.guardia

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.Calendar

/*
 Expense Entry Page Activity
 This activity provides a form for users to log a new expense.
 Required fields:
 -Expense Name
 -Amount
 -Category
 -Date (selected via a DatePicker)
 -Description (optional)
 Once saved, the expense is linked to the logged-in user's ID in the database.
 */
class expense_page : AppCompatActivity() {

    // UI component references
    lateinit var etExpenseName: EditText
    lateinit var etExpenseAmount: EditText
    lateinit var etExpenseCategory: EditText
    lateinit var etExpenseDate: EditText
    lateinit var etExpenseDescription: EditText
    lateinit var btnSaveExpense: Button
    lateinit var btnBackToDashboard: Button
    lateinit var btnExpenseMenu: ImageButton
    lateinit var btnExpenseSettings: ImageButton

    // Logged-in user context
    var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_page)

        // UI Customization: Hide the action bar
        supportActionBar?.hide()

        // Capture User ID passed from the dashboard
        userId = intent.getLongExtra("USER_ID", -1)

        // Initialize UI components, listeners, and specialized input helpers
        initializeViews()
        setupClickListeners()
        setupDatePicker()
    }


     //Binds UI components from activity_expense_page.xml to the activity variables.
    fun initializeViews() {
        etExpenseName = findViewById(R.id.etExpenseName)
        etExpenseAmount = findViewById(R.id.etExpenseAmount)
        etExpenseCategory = findViewById(R.id.etExpenseCategory)
        etExpenseDate = findViewById(R.id.etExpenseDate)
        etExpenseDescription = findViewById(R.id.etExpenseDescription)
        btnSaveExpense = findViewById(R.id.btnSaveExpense)
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard)
        btnExpenseMenu = findViewById(R.id.btnExpenseMenu)
        btnExpenseSettings = findViewById(R.id.btnExpenseSettings)
    }

    /*
     Configures the Date field to open an Android system DatePickerDialog when clicked.
     This ensures date formatting is consistent (DD/MM/YYYY).
     */
    fun setupDatePicker() {
        etExpenseDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Formats date with leading zeros where necessary
                    val formattedDate = String.format(
                        "%02d/%02d/%04d",
                        selectedDay,
                        selectedMonth + 1,
                        selectedYear
                    )
                    etExpenseDate.setText(formattedDate)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }


     //Defines the behavior for buttons and interactive elements.
    fun setupClickListeners() {
        // Collect form data and initiate save if valid
        btnSaveExpense.setOnClickListener {
            val name = etExpenseName.text.toString().trim()
            val amountText = etExpenseAmount.text.toString().trim()
            val category = etExpenseCategory.text.toString().trim()
            val date = etExpenseDate.text.toString().trim()
            val description = etExpenseDescription.text.toString().trim()

            // Basic validation check
            if (name.isEmpty() || amountText.isEmpty() || category.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val amount = amountText.toDouble()
                    saveExpense(name, amount, category, date, description)
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Return to the dashboard without saving
        btnBackToDashboard.setOnClickListener {
            finish() // Return to the previous screen (Dashboard)
        }

        // Notifications shortcut placeholder
        btnExpenseMenu.setOnClickListener {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
        }

        // Theme settings shortcut placeholder
        btnExpenseSettings.setOnClickListener {
            Toast.makeText(this, "Theme settings", Toast.LENGTH_SHORT).show()
        }
    }

    /*
     Persists the expense record to the database via the Repository.
     Uses a background coroutine to keep the UI responsive.
     */
    fun saveExpense(name: String, amount: Double, category: String, date: String, description: String) {
        lifecycleScope.launch {
            try {
                val application = application as GuardiaApplication
                application.repository.addExpense(userId, name, amount, category, date, description)

                Toast.makeText(this@expense_page, "Expense saved successfully!", Toast.LENGTH_SHORT).show()
                
                // Clear inputs and return to dashboard upon success
                clearFields()
                finish()

            } catch (e: Exception) {
                // UI notification on failure
                Toast.makeText(this@expense_page, "Error saving expense: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


     //Resets all input fields to their initial empty state.
    fun clearFields() {
        etExpenseName.text.clear()
        etExpenseAmount.text.clear()
        etExpenseCategory.text.clear()
        etExpenseDate.text.clear()
        etExpenseDescription.text.clear()
    }
}