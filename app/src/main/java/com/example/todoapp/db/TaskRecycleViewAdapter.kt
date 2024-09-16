package com.example.todoapp.db

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.db.Task

class TaskRecycleViewAdapter(
    private val clickListener:(Task)->Unit
): RecyclerView.Adapter<TaskViewHolder>() {

    private val taskList = ArrayList<Task>()
    var selectedTask: Task? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item, parent, false)
        return TaskViewHolder(listItem) // здесь мы создаем возможность прикрепления xml файлы
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task, clickListener)
        //holder.bind(taskList[position], clickListener)
        // На какой задаче фокус, та задача подсвечивается

        holder.itemView.setOnClickListener{ // задаем selectedTask
            // снимает фокус при повторном нажатии на выбраную задачу
            if(selectedTask == task){
                selectedTask = null
                notifyItemChanged(position)
            }
            // Не позволяет выбирать больше одной задачи
            else if(selectedTask == null) {
                selectedTask = task
                clickListener(task)
                notifyItemChanged(position) // работает ли ?
            }
        }

        if (task == selectedTask) {
            holder.itemView.setBackgroundColor(Color.WHITE) // Выбранный элемент
        } else {
            holder.itemView.setBackgroundColor(Color.BLACK) // Не выбранный элемент
        }


    }

    fun setList(tasks:List<Task>){
        taskList.clear()
        taskList.addAll(tasks)
    }

}



class TaskViewHolder(private val view: View): RecyclerView.ViewHolder(view){ // здесь xml файл получает значения
    fun bind(task: Task, clickListener: (Task) -> Unit){
        val nameTV = view.findViewById<TextView>(R.id.tvTaskName)
        val typeTV = view.findViewById<TextView>(R.id.tvTaskType)
        nameTV.text = task.name
        typeTV.text = task.type
        view.setOnClickListener{
            clickListener(task)
        }
    }
}