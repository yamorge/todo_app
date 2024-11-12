package com.example.todoapp.complete_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.time.LocalDate
import java.util.Date

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

    @Query("SELECT task_type, COUNT(*) AS count FROM complete_tasks_data_table GROUP BY task_type")
    suspend fun getTypeCounts(): List<TypeCount>

    @Query("SELECT task_type, COUNT(*) AS count FROM complete_tasks_data_table WHERE task_date LIKE :year || '%' GROUP BY task_type")
    suspend fun getTypeCountsForYear(year: Int): List<TypeCount>

    @Query("SELECT task_type, COUNT(*) AS count FROM complete_tasks_data_table WHERE task_date LIKE :year || '-' || :month || '%' GROUP BY task_type") // подсчитывает сколько раз встречается каждый task_type, учитывая при этом только определенные месяцы и годы
    suspend fun getTypeCountsForMonth(year: Int, month: Int): List<TypeCount>
}

data class TypeCount( // кастомный класс для хранения типа задач и числа его использований
    val task_type: String,
    val count: Int
)