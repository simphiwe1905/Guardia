package com.example.guardia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guardia.database.Expense

/*
ExpenseAdapter
 A RecyclerView Adapter responsible for managing and displaying a list of [Expense] items.
 It handles the creation of view holders and binding data to the UI components of each item.
 @param expenses Initial list of expenses to display.
 @param onDeleteClick Callback function triggered when the delete button of an expense item is clicked.
 */
class ExpenseAdapter(
    private var expenses: List<Expense>,
    private val onDeleteClick: (Expense) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    /*
     ViewHolder class that holds references to the UI components for an individual expense item.
     This improves performance by avoiding repeated calls to findViewById.
     */
    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvExpenseName: TextView = itemView.findViewById(R.id.tvExpenseName)
        val tvExpenseAmount: TextView = itemView.findViewById(R.id.tvExpenseAmount)
        val tvExpenseCategory: TextView = itemView.findViewById(R.id.tvExpenseCategory)
        val tvExpenseDate: TextView = itemView.findViewById(R.id.tvExpenseDate)
        val btnDeleteExpense: ImageButton = itemView.findViewById(R.id.btnDeleteExpense)
    }


     //Inflates the layout for a single item in the RecyclerView.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        // Inflate activity_item_expense.xml as the layout for each row
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_expense, parent, false)
        return ExpenseViewHolder(view)
    }


     //Binds data from the expense list to the UI components of the ViewHolder.
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]

        // Populate text fields with expense data
        holder.tvExpenseName.text = expense.name
        // Format the currency amount (e.g., R 1,234.56)
        holder.tvExpenseAmount.text = String.format("R %,.2f", expense.amount)
        holder.tvExpenseCategory.text = expense.category
        holder.tvExpenseDate.text = expense.date

        // Set up the delete button click listener
        holder.btnDeleteExpense.setOnClickListener {
            onDeleteClick(expense)
        }

        // Open detail view on item click
        holder.itemView.setOnClickListener {
            // Could navigate to a detailed view
        }
    }


     //Returns the total number of items in the list.
    override fun getItemCount(): Int = expenses.size

    /*
     Updates the adapter's data set and refreshes the RecyclerView.
     @param newExpenses The new list of expenses to display.
     */
    fun updateExpenses(newExpenses: List<Expense>) {
        expenses = newExpenses
        notifyDataSetChanged()
    }
}
