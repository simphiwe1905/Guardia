package com.example.guardia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class goals_page : AppCompatActivity() {

    lateinit var btnBackToDashboard: Button
    lateinit var btnMenu: ImageButton
    lateinit var btnSettings: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goals_page)

        initializeViews()
        setupClickListeners()
    }

    fun initializeViews() {
        // These IDs are placeholders - you'll need to match them with your actual goals_page layout
        // btnBackToDashboard = findViewById(R.id.btnBackToDashboard)
        // btnMenu = findViewById(R.id.btnMenu)
        // btnSettings = findViewById(R.id.btnSettings)
    }

    fun setupClickListeners() {
        btnBackToDashboard.setOnClickListener {
            val intent = Intent(this, dashboard::class.java)
            startActivity(intent)
            finish()
        }

        btnMenu.setOnClickListener {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show()
        }

        btnSettings.setOnClickListener {
            Toast.makeText(this, "Theme settings", Toast.LENGTH_SHORT).show()
        }
    }
}