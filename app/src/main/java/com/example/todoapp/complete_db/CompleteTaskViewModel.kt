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

}