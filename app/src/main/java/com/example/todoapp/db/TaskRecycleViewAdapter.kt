package com.example.todoapp.db

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.db.Task

class TaskRecycleViewAdapter(): RecyclerView.Adapter<TaskViewHolder>() {

    private val taskList = ArrayList<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item, parent, false)
        return TaskViewHolder(listItem) // здесь мы создаем возможность прикрепления xml файлы
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    fun setList(tasks:List<Task>){
        taskList.clear()
        taskList.addAll(tasks)
    }

}



class TaskViewHolder(private val view: View): RecyclerView.ViewHolder(view){ // здесь xml файл получает значения
    fun bind(task: Task){
        val nameTV = view.findViewById<TextView>(R.id.tvTaskName)
        val typeTV = view.findViewById<TextView>(R.id.tvTaskType)
        nameTV.text = task.name
        typeTV.text = task.type
            //view.setOnClickListener{
            //clickListener(task)
        //}
    }
}