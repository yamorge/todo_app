package com.example.todoapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.compose.ui.window.application
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTodoBinding
import com.example.todoapp.db.Task
import com.example.todoapp.db.TaskDatabase
import com.example.todoapp.db.TaskRecycleViewAdapter
import com.example.todoapp.db.TaskViewModel
import com.example.todoapp.db.TaskViewModelFactory
import kotlinx.coroutines.selects.select


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodoFragment : Fragment() {

    private lateinit var binding: FragmentTodoBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter: TaskRecycleViewAdapter

    private var isListItemClicked = false
    private lateinit var selectedTask: Task

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = TaskDatabase.getInstance(requireActivity().application).taskDao()
        val factory = TaskViewModelFactory(dao)
        viewModel = ViewModelProvider(requireActivity(), factory).get(TaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        // Теперь вы можете использовать binding для доступа к элементам интерфейса
        binding.btnAddTask.setOnClickListener {
            if (isListItemClicked == false) {
                val text = binding.etAddTask.text
                addTask(text.toString())

            }
            else{
                deleteTask()
            }
        }
        // Другие операции с элементами интерфейса

        // Снимаем фокус с задачи при нажатии на бэкграунд
        binding.root.setOnClickListener {
            unfocus()
        }

    }

    private fun addTask(text: String){ // изменить потом
        viewModel.insertTask(
            Task(
                0,
                text,
                "sport"
            )
        )
    }

    private fun deleteTask(){
        viewModel.deleteTask(Task(
            selectedTask.id,
            selectedTask.name,
            selectedTask.type
        ))
        unfocus() // таким образом фокус не застревает на удаленной задаче
    }

    private fun initRecyclerView(){ // Вызывается лишь однажды, инициализирует RecyclerView

        val taskRV = binding.rvTasks
        taskRV.layoutManager = LinearLayoutManager(requireContext())
       // Вызывается каждый раз при клике и обновляется соответственно в дисплейтасклист
        adapter = TaskRecycleViewAdapter{
            selectedItem: Task -> listItemClicked(selectedItem)
        }
        taskRV.adapter = adapter

        displayTasksList() // разделяем логику на разные функции
    }

    private fun displayTasksList(){
        viewModel.tasks.observe(requireActivity(), { tasks ->
            adapter.setList(tasks)
            adapter.notifyDataSetChanged() // заменить после теста!
        })
    }

    private fun listItemClicked(task: Task){
        selectedTask = task
        isListItemClicked = true
    }

    private fun unfocus(){
        adapter.selectedTask = null
        adapter.notifyDataSetChanged() // поменять после теста!
    }

}