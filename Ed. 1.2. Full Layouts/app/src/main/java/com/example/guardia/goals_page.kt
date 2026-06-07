package com.example.guardia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class goals_page : AppCompatActivity() {

    lateinit var etGoalName: EditText
    lateinit var etTargetAmount: EditText
    lateinit var etGoalMonth: EditText
    lateinit var etMinAmount: EditText
    lateinit var etMaxAmount: EditText
    lateinit var btnSaveGoal: Button
    lateinit var btnBackToDashboard: Button

    var userId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals_page)

        supportActionBar?.hide()

        userId = intent.getLongExtra("USER_ID", -1)

        initializeViews()
        setupClickListeners()
    }

    fun initializeViews() {
        etGoalName = findViewById(R.id.etGoalName)
        etTargetAmount = findViewById(R.id.etTargetAmount)
        etGoalMonth = findViewById(R.id.etGoalMonth)
        etMinAmount = findViewById(R.id.etMinAmount)
        etMaxAmount = findViewById(R.id.etMaxAmount)
        btnSaveGoal = findViewById(R.id.btnSaveGoal)
        btnBackToDashboard = findViewById(R.id.btnBackToDashboard)
    }

    fun setupClickListeners() {
        btnSaveGoal.setOnClickListener {
            val goalName = etGoalName.text.toString().trim()
            val targetAmountText = etTargetAmount.text.toString().trim()
            val monthYear = etGoalMonth.text.toString().trim()
            val minAmountText = etMinAmount.text.toString().trim()
            val maxAmountText = etMaxAmount.text.toString().trim()

            if (goalName.isEmpty() || targetAmountText.isEmpty() || monthYear.isEmpty() ||
                minAmountText.isEmpty() || maxAmountText.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    val targetAmount = targetAmountText.toDouble()
                    val minAmount = minAmountText.toDouble()
                    val maxAmount = maxAmountText.toDouble()

                    if (minAmount >= maxAmount) {
                        Toast.makeText(this, "Minimum must be less than maximum", Toast.LENGTH_SHORT).show()
                    } else {
                        saveAllGoals(goalName, targetAmount, monthYear, minAmount, maxAmount)
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Please enter valid amounts", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnBackToDashboard.setOnClickListener {
            val intent = Intent(this, dashboard::class.java)
            intent.putExtra("USER_ID", userId)
            startActivity(intent)
            finish()
        }
    }

    fun saveAllGoals(goalName: String, targetAmount: Double, monthYear: String, minAmount: Double, maxAmount: Double) {
        lifecycleScope.launch {
            try {
                val application = application as GuardiaApplication

                // Save savings goal
                application.repository.addSavingsGoal(userId, goalName, targetAmount)

                // Save monthly spending goal
                application.repository.setMonthlyGoal(userId, monthYear, minAmount, maxAmount)

                Toast.makeText(this@goals_page, "Goals saved successfully!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@goals_page, dashboard::class.java)
                intent.putExtra("USER_ID", userId)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@goals_page, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}