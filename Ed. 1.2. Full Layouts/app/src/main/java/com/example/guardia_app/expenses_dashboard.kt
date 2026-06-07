package com.example.guardia_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class expenses_dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_expenses_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

/**
        //First initialize the spinner with the data selection periods
        val sp_dateSelector = findViewById<Spinner>(R.id.sp_DateSelector)

        //These lines are purely for testing
        val testManager = ExpensesManager()
        val testData = testManager.generateTestExpenses(70)
        val dataSelections = testManager.aggregateDataSet(testData)

        val selectorAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            dataSelections
        )
        selectorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_dateSelector.adapter = selectorAdapter
*/
    }

    fun homePage(view: View) {
        val pageSwitch = Intent(this@expenses_dashboard, main_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun goalsPage(view: View){
        val pageSwitch = Intent(this@expenses_dashboard, goals_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun settingsPage(view: View){
        val pageSwitch = Intent(this@expenses_dashboard, settings_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun awardsPage(view: View){
        val pageSwitch = Intent(this@expenses_dashboard, awards_dashboard::class.java)
        startActivity(pageSwitch)
    }

    fun categoriesPage(view: View){
        val pageSwitch = Intent(this@expenses_dashboard, categories_dashboard::class.java)
        startActivity(pageSwitch)
    }
}