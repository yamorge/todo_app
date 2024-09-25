package com.example.todoapp.complete_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "complete_tasks_data_table")
data class CompleteTask(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    var id: Int,
    @ColumnInfo(name = "task_name")
    var name: String,
    @ColumnInfo(name = "task_type")
    var type: String
)