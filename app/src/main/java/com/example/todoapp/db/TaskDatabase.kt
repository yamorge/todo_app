package com.example.todoapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase: RoomDatabase(){

    abstract fun taskDao():TaskDao
    companion object{
        @Volatile
        private var INSTANCE: TaskDatabase? = null
        fun getInstance(context: Context):TaskDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null) { // если база данных еще не создана
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TaskDatabase::class.java,
                        "tasks_data_database"
                    ).build()
                }
                return instance
            }
        }
    }
}

