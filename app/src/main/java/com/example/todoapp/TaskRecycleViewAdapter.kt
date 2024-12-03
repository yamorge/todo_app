package com.example.todoapp

import android.app.PendingIntent.getActivity
import android.graphics.Color
import android.graphics.Paint
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.db.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

class TaskRecycleViewAdapter(
    private val listener: TaskInteractionListener,
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
        val checkBox = holder.itemView.findViewById<CheckBox>(R.id.cbComplete)
        val cardView: CardView = holder.itemView.findViewById(R.id.cvListItem)
        val nameTV = holder.itemView.findViewById<TextView>(R.id.tvTaskName)


        holder.bind(task, clickListener)
        //holder.bind(taskList[position], clickListener)
        // На какой задаче фокус, та задача подсвечивается

        holder.itemView.setOnClickListener{ // задаем selectedTask
            // снимает фокус при повторном нажатии на выбраную задачу
            if(selectedTask == task){
                selectedTask = null
                listener.unfocus()
                notifyItemChanged(position)
            }
            // Не позволяет выбирать больше одной задачи
            else if(selectedTask == null) {
                selectedTask = task
                clickListener(task)
                notifyItemChanged(position)
            }
        }

        checkBox.setOnClickListener(null) // сбрасываем предыдущий

        checkBox.setOnClickListener {

                listener.completeTask(task)
                checkBox.isChecked = false

        }

        // Когда меняем цвет, мы используем setCardBackgroundColor, потому что только так можно сохранить закругление углов

        if (task.type == "social"){
            cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.social))
        }
        else if(task.type == "sport"){
            cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.sport))
        }
        else if(task.type == "studying"){
            cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.studying))
        }
        else{
            cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.hobbies))
        }

        if (task == selectedTask) { // Поменять потом
            cardView.setCardBackgroundColor(Color.rgb(239, 208, 202)) // Выбранный элемент
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
        val checkBox = view.findViewById<CheckBox>(R.id.cbComplete)
        nameTV.text = task.name
        //typeTV.text = task.type
        view.setOnClickListener{
            clickListener(task)
        }

    }


}