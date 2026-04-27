package com.example.guardia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

/*
 Registration Page Activity
 This activity allows new users to create an account by providing:
 -Username
 -Email address
 -Password (and confirmation)
 Validation is performed to ensure:
 -All fields are filled.
 -Passwords match.
 -Email format is valid.
 -Password complexity (length) is met.
 -Username and Email are unique in the database.
 */
class register_page : AppCompatActivity() {

    // UI component references
    lateinit var etRegisterUsername: EditText
    lateinit var etRegisterEmail: EditText
    lateinit var etRegisterPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var btnBackToLogin: Button
    lateinit var btnMenu: ImageButton
    lateinit var btnSettings: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_page)

        // Initialize UI components and set up listeners
        initializeViews()
        setupClickListeners()
    }


    //Binds UI components from activity_register_page.xml to the activity variables.
    fun initializeViews() {
        etRegisterUsername = findViewById(R.id.etRegisterUsername)
        etRegisterEmail = findViewById(R.id.etRegisterEmail)
        etRegisterPassword = findViewById(R.id.etRegisterPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnBackToLogin = findViewById(R.id.btnBackToLogin)
        btnMenu = findViewById(R.id.btnMenu)
        btnSettings = findViewById(R.id.btnSettings)
    }


     //Configures the behavior for buttons and interactive elements.

    fun setupClickListeners() {
        // Validation and registration logic
        btnRegister.setOnClickListener {
            val username = etRegisterUsername.text.toString().trim()
            val email = etRegisterEmail.text.toString().trim()
            val password = etRegisterPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            when {
                username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
                password != confirmPassword -> {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    registerUser(username, email, password)
                }
            }
        }

        // Return to login screen
        btnBackToLogin.setOnClickListener {
            finish() // Close registration to return to login
        }

        // Notification shortcut placeholder
        btnMenu.setOnClickListener {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
        }

        // Settings shortcut placeholder
        btnSettings.setOnClickListener {
            Toast.makeText(this, "Theme settings", Toast.LENGTH_SHORT).show()
        }
    }

    /*
     Checks for existing user data and creates a new user record in the database.
     Performed on a background thread using coroutines.
     */
    fun registerUser(username: String, email: String, password: String) {
        lifecycleScope.launch {
            try {
                val application = application as GuardiaApplication

                // Identity uniqueness check: Username
                if (application.repository.isUsernameTaken(username)) {
                    Toast.makeText(this@register_page, "Username already taken", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Identity uniqueness check: Email
                if (application.repository.isEmailTaken(email)) {
                    Toast.makeText(this@register_page, "Email already registered", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                // Success: Persistence and redirection
                application.repository.registerUser(username, email, password)
                Toast.makeText(this@register_page, "Account created successfully! Please login.", Toast.LENGTH_SHORT).show()

                finish() // Successfully registered, return to login screen

            } catch (e: Exception) {
                // UI notification on database or unexpected errors
                Toast.makeText(this@register_page, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}