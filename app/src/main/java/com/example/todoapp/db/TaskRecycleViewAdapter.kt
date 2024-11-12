package com.example.todoapp.db

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.TaskInteractionListener
import com.example.todoapp.TodoFragment
import com.example.todoapp.db.Task

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
                notifyItemChanged(position) // работает ли ?
            }
        }

        checkBox.setOnClickListener(null) // сбрасываем предыдущий

        checkBox.setOnClickListener {  // Здесь добавляется анимация при выполнении чекбокса и вызывается функция из фрагмента (при помощи интерфейса)
            selectedTask = task // нужно для того, чтобы фокус переходил на задачу с чекбоксом
            holder.itemView.animate()
                .alpha(0f)
                .setDuration(300)
                .withEndAction {
                    listener.completeTask(selectedTask)
                }
                .start()
        }

        // Когда меняем цвет, мы используем setCardBackgroundColor, потому что только так можно сохранить закругление углов

        if (task.type == "social"){
            cardView.setCardBackgroundColor(Color.parseColor("#8689AC"))
        }
        else if(task.type == "sport"){
            cardView.setCardBackgroundColor(Color.parseColor("#587099"))
        }
        else if(task.type == "studying"){
            cardView.setCardBackgroundColor(Color.parseColor("#3F5576"))
        }
        else{
            cardView.setCardBackgroundColor(Color.parseColor("#2F3148"))
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