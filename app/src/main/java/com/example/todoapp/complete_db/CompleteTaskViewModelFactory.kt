package com.example.todoapp.complete_db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CompleteTaskViewModelFactory(
    private val dao: CompleteTaskDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CompleteTaskViewModel::class.java)) {
            return CompleteTaskViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}