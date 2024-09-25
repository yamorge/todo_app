package com.example.todoapp.complete_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.todoapp.complete_db.CompleteTask
import com.example.todoapp.complete_db.CompleteTaskDao

@Database(entities = [CompleteTask::class], version = 1, exportSchema = false)
abstract class CompleteTaskDatabase: RoomDatabase(){

    abstract fun completeTaskDao(): CompleteTaskDao
    companion object{
        @Volatile
        private var INSTANCE: CompleteTaskDatabase? = null
        fun getInstance(context: Context): CompleteTaskDatabase {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null) { // если база данных еще не создана
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CompleteTaskDatabase::class.java,
                        "complete_tasks_data_database"
                    ).build()
                }
                return instance
            }
        }
    }
}