package com.example.guardia.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "expenses",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE // When user is deleted, their expenses go with them — prevents orphaned records
        )
    ],
    indices = [Index("user_id")] // Index speeds up queries filtering by userId — essential for performance as expenses grow
)
data class Expense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // 0 as default lets Room auto-generate; 0 means "not yet inserted"

    @ColumnInfo(name = "user_id")
    val userId: Long, // Links to User table — matches parentColumns above

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "amount")
    val amount: Double,

    @ColumnInfo(name = "category")
    val category: String, // Categorizing expenses helps analytics. Could become an enum later.

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "description")
    val description: String = "", // Default empty string prevents null issues

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis() // Timestamp auto-set when object is created, not when inserted into DB
)