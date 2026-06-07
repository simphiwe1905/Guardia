package com.example.guardia.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "monthly_goals",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("user_id")]
)
data class MonthlyGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "month_year")
    val monthYear: String,

    @ColumnInfo(name = "min_amount")
    val minAmount: Double,

    @ColumnInfo(name = "max_amount")
    val maxAmount: Double,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)