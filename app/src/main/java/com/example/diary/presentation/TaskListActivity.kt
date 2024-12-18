package com.example.diary.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarWeekDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.example.diary.DiaryApp
import com.example.diary.R
import com.example.diary.databinding.ActivityTaskListBinding
import javax.inject.Inject

class TaskListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTaskListBinding
    private val component by lazy { (application as DiaryApp).component }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(TaskListViewModel::class)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendarView = binding.calendar.apply {
            setFirstDayOfWeek(CalendarWeekDay.MONDAY)
        }

        val taskViewManager = TaskViewManager(this, binding.tasksCL, calendarView)

        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {

            override fun onClick(calendarDay: CalendarDay) {
                taskViewManager.showTasksOfTheDay(calendarDay)
            }
        })

        viewModel.taskListLD.observe(this, {
            taskViewManager.taskList = it
        })

        binding.calendarIV.setOnClickListener {
                if (calendarView.visibility == View.GONE) {
                    calendarView.visibility = View.VISIBLE
                } else calendarView.visibility = View.GONE
        }

        binding.sleepingCatIV.setOnClickListener {
            Toast.makeText(this, getString(R.string.meow), Toast.LENGTH_SHORT).show()
        }

        binding.floatingActionButton.setOnClickListener {
            val time = calendarView.selectedDates[0].timeInMillis / 1000
            startActivity(AddTaskActivity.getAddTaskScreenIntent(this, time))
        }


    }
}