package com.example.todoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.complete_db.CompleteTask
import com.example.todoapp.databinding.FragmentTodoBinding
import com.example.todoapp.complete_db.CompleteTaskDatabase
import com.example.todoapp.complete_db.CompleteTaskViewModel
import com.example.todoapp.complete_db.CompleteTaskViewModelFactory
import com.example.todoapp.db.Task
import com.example.todoapp.db.TaskDatabase
import com.example.todoapp.db.TaskViewModel
import com.example.todoapp.db.TaskViewModelFactory
import kotlinx.coroutines.delay
import java.time.LocalDate


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TodoFragment : Fragment(), TaskInteractionListener {

    private lateinit var binding: FragmentTodoBinding
    private lateinit var viewModel: TaskViewModel
    private lateinit var completeViewModel: CompleteTaskViewModel
    private lateinit var adapter: TaskRecycleViewAdapter

    private var isListItemClicked = false
    private lateinit var selectedTask: Task

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
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        binding.btnAddTask.setOnClickListener {
            if (isListItemClicked == false) {
                replaceFragment()
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
       // Вызывается каждый раз при клике и обновляется соответственно в displayTaskList
        adapter = TaskRecycleViewAdapter(this){ // передаем слушатель интерфейса и кликлисенер
            selectedItem: Task -> listItemClicked(selectedItem)
        }
        taskRV.adapter = adapter

        displayTasksList() // разделяем логику на разные функции
    }

    private fun displayTasksList(){
        viewModel.tasks.observe(requireActivity(), { tasks ->
            adapter.setList(tasks)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(task: Task){
        selectedTask = task
        isListItemClicked = true
        binding.btnAddTask.text = getString(R.string.delete_task) // Всякий раз, когда задача выбирается, меняем кнопку
    }

   override fun unfocus(){
        adapter.selectedTask = null
        isListItemClicked = false
        adapter.notifyDataSetChanged()
        binding.btnAddTask.text = getString(R.string.add_task) // Меняем кнопку назад, всякий раз, когда не выбрана задача

   }

    override fun completeTask(selectedTask: Task?){
        if (selectedTask != null) {
            completeViewModel.insertTask(
                CompleteTask(
                    selectedTask.id,
                    selectedTask.name,
                    selectedTask.type,
                    LocalDate.now().toString()
                )
            )

            viewModel.deleteTask(
                Task(
                    selectedTask.id,
                    selectedTask.name,
                    selectedTask.type
                )
            )
            unfocus()

        }
    }

    private fun replaceFragment(){
        val addTaskFragment = AddTaskFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.flFragment, addTaskFragment)
            .addToBackStack(null) // позволяет при нажатии назад вернуться в предыдущий фрагмент
            .commit()
    }

}