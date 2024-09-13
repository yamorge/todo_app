package com.example.todoapp.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.db.Task
import com.example.todoapp.db.TaskDao
import kotlinx.coroutines.launch

class TaskViewModel(private val dao: TaskDao): ViewModel() {

    val tasks = dao.getAllTasks()

    fun insertTask(task: Task)=viewModelScope.launch { // ViewModelScope вызывает CoroutineScope, а значит выполняет функции на фоне
        dao.insertTask(task)
    }

    fun updateTask(task: Task)=viewModelScope.launch {
        dao.updateTask(task)
    }

    fun deleteTask(task: Task)=viewModelScope.launch {
        dao.deleteTask(task)
    }

}