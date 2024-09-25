package com.example.todoapp.complete_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CompleteTaskDao {
    @Insert
    suspend fun insertTask(task: CompleteTask)

    @Update
    suspend fun updateTask(task: CompleteTask)

    @Delete
    suspend fun deleteTask(task: CompleteTask)

    @Query("SELECT * FROM complete_tasks_data_table")
    fun getAllTasks(): LiveData<List<CompleteTask>>
}