package com.example.guardia_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class awards_dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_awards_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun expensePage(view: View) {
        val pageSwitch = Intent(this@awards_dashboard, expenses_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun goalsPage(view: View){
        val pageSwitch = Intent(this@awards_dashboard, goals_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun homePage(view: View){
        val pageSwitch = Intent(this@awards_dashboard, main_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun settingsPage(view: View){
        val pageSwitch = Intent(this@awards_dashboard, settings_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun categoriesPage(view: View){
        val pageSwitch = Intent(this@awards_dashboard, categories_dashboard::class.java)
        startActivity(pageSwitch)
    }
}