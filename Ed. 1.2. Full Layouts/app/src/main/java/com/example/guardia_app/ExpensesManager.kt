package com.example.guardia_app

import android.os.Build
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

public class ExpensesManager {
    fun aggregateDataSet(expenses: List<Expenses>): Array<String>{
        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("MMMM yyyy")
        } else {
            TODO("VERSION.SDK_INT < O")
        } // e.g. "May 2026"
        var selectablePeriods = listOf(
            LocalDate.parse(expenses.first().date).format(formatter)
        )

        for (expense in expenses) {
            val period = LocalDate.parse(expense.date).format(formatter)
            if (period !in selectablePeriods) {
                selectablePeriods = selectablePeriods + period
            }
        }

        return selectablePeriods.toTypedArray()
    }

    fun generateTestExpenses(count: Int): List<Expenses> {
        val names = listOf("Groceries", "Electricity Bill", "Movie Night", "Gym Membership", "Coffee Run", "Taxi Fare", "Concert Ticket", "Rent", "Water Bill", "Internet")
        val expenses = mutableListOf<Expenses>()

        repeat(count) {
            val year = Random.nextInt(2015, 2026) // random year between 2015 and 2025
            val month = Random.nextInt(1, 13)     // 1–12
            val day = Random.nextInt(1, 28)       // keep it safe for all months
            val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LocalDate.of(year, month, day).toString()
            } else {
                TODO("VERSION.SDK_INT < O")
            }

            val name = names.random()
            val amount = "R ${Random.nextInt(50, 5000)}.00"

            expenses.add(Expenses(name, date, amount))
        }
        return expenses
    }
}