package com.example.todoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.complete_db.CompleteTaskDatabase
import com.example.todoapp.complete_db.CompleteTaskViewModel
import com.example.todoapp.complete_db.CompleteTaskViewModelFactory
import com.example.todoapp.databinding.FragmentAddTaskBinding
import com.example.todoapp.db.Task
import com.example.todoapp.db.TaskDatabase
import com.example.todoapp.db.TaskViewModel
import com.example.todoapp.db.TaskViewModelFactory

class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var completeViewModel: CompleteTaskViewModel
    private lateinit var adapter: TaskRecycleViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = TaskDatabase.getInstance(requireActivity().application).taskDao()
        val factory = TaskViewModelFactory(dao)
        viewModel = ViewModelProvider(requireActivity(), factory).get(TaskViewModel::class.java)

        val completeDao = CompleteTaskDatabase.getInstance(requireActivity().application).completeTaskDao()
        val completeFactory = CompleteTaskViewModelFactory(completeDao)
        completeViewModel = ViewModelProvider(requireActivity(), completeFactory).get(CompleteTaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var type = ""
        val radioGroup = binding.rgTypes

        radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            when(checkedId) {
                R.id.rbTypeStudying -> {
                    type = "studying"
                }
                R.id.rbTypeSport -> {
                    type = "sport"
                }
                R.id.rbTypeHobbies -> {
                    type = "hobbies"
                }
                R.id.rbTypeSocial -> {
                    type = "social"
                }
            }
        }

        binding.btnSubmit.setOnClickListener{
            val text = binding.etEnterTask.text.toString()
            if (text == ""){ // Проверяем, чтобы текст и тип задачи не были пустыми
                Toast.makeText(requireContext(), "Task is empty", Toast.LENGTH_SHORT).show()
            }
            else if(type == ""){
                Toast.makeText(requireContext(), "Type is empty", Toast.LENGTH_SHORT).show()
            }
            else {
                addTask(text, type)
                replaceFragment()
            }
        }

    }

    private fun addTask(text: String, type: String){ // изменить потом
        viewModel.insertTask(
            Task(
                0,
                text,
                type
            )
        )
    }

    private fun replaceFragment(){
        val todoFragment = TodoFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, todoFragment)
            .commit()
    }


    }