package com.example.todoapp

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

        // Теперь вы можете использовать binding для доступа к элементам интерфейса
        binding.btnAddTask.setOnClickListener {
            val text = binding.etAddTask.text
            addTask(text.toString())
            initRecyclerView()
        }
        // Другие операции с элементами интерфейса
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

    private fun initRecyclerView(){

        val taskRV = binding.rvTasks
        taskRV.layoutManager = LinearLayoutManager(requireContext())
        adapter = TaskRecycleViewAdapter()
        taskRV.adapter = adapter

        displayTasksList() // разделяем логику на разные функции
    }

    private fun displayTasksList(){
        viewModel.tasks.observe(requireActivity(), { tasks ->
            adapter.setList(tasks)
            adapter.notifyDataSetChanged() // заменить после теста!
        })
    }


}