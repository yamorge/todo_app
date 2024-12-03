package com.example.todoapp

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.R
import com.example.todoapp.complete_db.CompleteTaskDatabase
import com.example.todoapp.complete_db.CompleteTaskViewModel
import com.example.todoapp.complete_db.CompleteTaskViewModelFactory
import com.example.todoapp.databinding.FragmentStatisticsBinding
import com.example.todoapp.databinding.FragmentTodoBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.time.LocalDate

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StatisticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatisticsFragment : Fragment() {
    private lateinit var binding: FragmentStatisticsBinding
    private lateinit var pieChart: PieChart
    private lateinit var completeViewModel: CompleteTaskViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val completeDao = CompleteTaskDatabase.getInstance(requireActivity().application).completeTaskDao()
        val completeFactory = CompleteTaskViewModelFactory(completeDao)
        completeViewModel = ViewModelProvider(requireActivity(), completeFactory).get(CompleteTaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        pieChart = binding.pieChart
        loadChartData(getAllDataFromDatabase())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMonth.setOnClickListener {
            loadChartData(getMonthDataFromDatabase())
        }
        binding.btnYear.setOnClickListener {
            loadChartData(getYearDataFromDatabase())
        }
        binding.btnAllTime.setOnClickListener {
            loadChartData(getAllDataFromDatabase())
        }
    }

    private fun loadChartData(data: List<Pair<String, Int>>){
        val customColors = listOf(
            resources.getColor(R.color.studying),
            resources.getColor(R.color.hobbies),
            resources.getColor(R.color.sport),
            resources.getColor(R.color.social)
        )



        // Создаем список входных данных для PieChart
        val entries = ArrayList<PieEntry>()
        for((label, value) in data){
            entries.add(PieEntry(value.toFloat(), label))
        }

        //Создаем набор данных и настраиваем диаграмму
        val dataSet = PieDataSet(entries, "Statistics")
        dataSet.colors = customColors
        dataSet.valueTextSize = 20f
        dataSet.setSliceSpace(6f)
        dataSet.valueTypeface = resources.getFont(R.font.moderustic_semibold)

        // Форматируем числовые значения
        dataSet.valueFormatter = object : ValueFormatter() {
            override fun getPieLabel(value: Float, entry: PieEntry): String {
                return NumberFormat.getInstance().format(value.toInt()) // Форматируем как целое число
            }
        }

        val pieData = PieData(dataSet)

        pieChart.data = pieData
        // Настройка визуала
        pieChart.holeRadius = 25f
        pieChart.transparentCircleRadius = 5f
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.animateY(600, Easing.EaseInOutQuad)
        pieChart.setEntryLabelTextSize(15f)
        pieChart.setEntryLabelTypeface(resources.getFont(R.font.moderustic_bold))
        pieChart.invalidate() // обновление диаграммы
    }

    private fun getAllDataFromDatabase(): List<Pair<String, Int>> {
            val typeList: MutableList<Pair<String, Int>> = mutableListOf()
            runBlocking {
                val typeCounts = completeViewModel.getTypeCounts()
                typeCounts.forEach{ typeCount ->
                    typeList.add(Pair(typeCount.task_type, typeCount.count))
                }
            }
        return typeList
    }

    private fun getMonthDataFromDatabase(): List<Pair<String, Int>> {
        val typeList: MutableList<Pair<String, Int>> = mutableListOf()
        runBlocking {
            val month = LocalDate.now().month.value // получаем цифровое значение текущего месяца
            val year = LocalDate.now().year
            val typeCounts = completeViewModel.getTypeCountsForMonth(year, month)
            typeCounts.forEach{ typeCount ->
                typeList.add(Pair(typeCount.task_type, typeCount.count))
            }
        }
        return typeList
    }

    private fun getYearDataFromDatabase(): List<Pair<String, Int>> {
        val typeList: MutableList<Pair<String, Int>> = mutableListOf()
        runBlocking {
            val year = LocalDate.now().year
            val typeCounts = completeViewModel.getTypeCountsForYear(year)
            typeCounts.forEach{ typeCount ->
                typeList.add(Pair(typeCount.task_type, typeCount.count))
            }
        }
        return typeList
    }

    }
