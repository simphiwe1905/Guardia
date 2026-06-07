package com.example.guardia_app

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class categories_dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categories_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun homePage(view: View) {
        val pageSwitch = Intent(this@categories_dashboard, main_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun expensePage(view: View) {
        val pageSwitch = Intent(this@categories_dashboard, expenses_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun goalsPage(view: View){
        val pageSwitch = Intent(this@categories_dashboard, goals_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun awardsPage(view: View){
        val pageSwitch = Intent(this@categories_dashboard, awards_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun settingsPage(view: View){
        val pageSwitch = Intent(this@categories_dashboard, settings_dashboard::class.java)
        startActivity(pageSwitch)
    }
}

