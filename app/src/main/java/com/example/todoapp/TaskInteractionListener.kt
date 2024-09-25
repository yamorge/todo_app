package com.example.todoapp

import com.example.todoapp.db.Task

interface TaskInteractionListener {
    fun completeTask(selectedTask: Task?)
    fun unfocus()
}