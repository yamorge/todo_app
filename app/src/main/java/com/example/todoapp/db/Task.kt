package com.example.todoapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks_data_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    var id: Int,
    @ColumnInfo(name = "task_name")
    var name: String,
    @ColumnInfo(name = "task_type")
    var type: String
)