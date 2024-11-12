package com.example.todoapp.complete_db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.todoapp.complete_db.CompleteTask


class CompleteTaskViewModel(private val dao: CompleteTaskDao): ViewModel() {

    val tasks = dao.getAllTasks()


    fun insertTask(task: CompleteTask)=viewModelScope.launch { // ViewModelScope вызывает CoroutineScope, а значит выполняет функции на фоне
        dao.insertTask(task)
    }

    fun updateTask(task: CompleteTask)=viewModelScope.launch {
        dao.updateTask(task)
    }

    fun deleteTask(task: CompleteTask)=viewModelScope.launch {
        dao.deleteTask(task)
    }

    suspend fun getTypeCounts(): List<TypeCount> {
        return dao.getTypeCounts() // мб поменять логику через репозиторий
    }

    suspend fun getTypeCountsForYear(year: Int): List<TypeCount> {
        return dao.getTypeCountsForYear(year) // мб поменять логику через репозиторий
    }

    suspend fun getTypeCountsForMonth(year: Int, month: Int): List<TypeCount> {
        return dao.getTypeCountsForMonth(year, month) // мб поменять логику через репозиторий
    }

}