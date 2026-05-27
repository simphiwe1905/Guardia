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
 Login Page Activity
 This activity handles user authentication. Users can:
 -Enter their username and password to log in.
 -Navigate to the Registration page to create a new account.
 It uses the GuardiaRepository to verify credentials against the local Room database.
 */
class login_page : AppCompatActivity() {

    // UI component references
    lateinit var etUsername: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var btnCreateAccount: Button
    lateinit var btnMenu: ImageButton
    lateinit var btnSettings: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        // Initialize UI components and set up listeners
        initializeViews()
        setupClickListeners()
    }


     //Binds UI components from activity_login_page.xml to the activity variables.
    fun initializeViews() {
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnCreateAccount = findViewById(R.id.btnCreateAccount)
        btnMenu = findViewById(R.id.btnMenu)
        btnSettings = findViewById(R.id.btnSettings)
    }


     //Configures the behavior for buttons and interactive elements.
    fun setupClickListeners() {
        // Trigger login process
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(username, password)
            }
        }

        // Navigate to Registration screen
        btnCreateAccount.setOnClickListener {
            val intent = Intent(this, register_page::class.java)
            startActivity(intent)
        }

        // Notification shortcut
        btnMenu.setOnClickListener {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
        }

        // Settings shortcut
        btnSettings.setOnClickListener {
            Toast.makeText(this, "Theme settings", Toast.LENGTH_SHORT).show()
        }
    }

    /*
     Authenticates the user by querying the database via the Repository.
     Uses a coroutine to perform the database operation on a background thread.
     */
    fun loginUser(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val application = application as GuardiaApplication
                val user = application.repository.loginUser(username, password)

                if (user != null) {
                    // Success: Show greeting and navigate to Dashboard with user context
                    Toast.makeText(this@login_page, "Welcome back, $username!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@login_page, dashboard::class.java)
                    intent.putExtra("USER_ID", user.id)
                    intent.putExtra("USERNAME", user.username)
                    startActivity(intent)
                    finish() // Close login page so user can't "back" into it
                } else {
                    // Failure: Clear password and notify user
                    Toast.makeText(this@login_page, "Invalid username or password", Toast.LENGTH_LONG).show()
                    etPassword.text.clear()
                }
            } catch (e: Exception) {
                // Unexpected error handling
                Toast.makeText(this@login_page, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}