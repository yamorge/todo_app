package com.example.todoapp.completed_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.db.Task
import com.example.todoapp.db.TaskDao

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class CompletedTasksDatabase: RoomDatabase(){

    abstract fun taskDao(): TaskDao
    companion object{
        @Volatile
        private var INSTANCE: CompletedTasksDatabase? = null
        fun getInstance(context: Context): CompletedTasksDatabase {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null) { // если база данных еще не создана
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CompletedTasksDatabase::class.java,
                        "completed_tasks_data_database"
                    ).build()
                }
                return instance
            }
        }
    }
}
