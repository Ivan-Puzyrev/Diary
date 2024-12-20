package com.example.diary.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.CalendarWeekDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.example.diary.DiaryApp
import com.example.diary.R
import com.example.diary.databinding.ActivityTaskListBinding
import java.time.ZoneId
import javax.inject.Inject

class TaskListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskListBinding
    private val component by lazy { (application as DiaryApp).component }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TaskListViewModel::class]
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

        val taskViewManager = TaskViewManager(this, binding.tasksCL)

        setupNavigationButtons(calendarView, taskViewManager)

        viewModel.taskListLD.observe(this) {
            taskViewManager.taskList = it
            calendarView.setCalendarDays(viewModel.getHighlightedDays())
            updateStateOfScreen(calendarView, taskViewManager)
        }

        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                calendarView.setDate(calendarDay.calendar)
                updateStateOfScreen(calendarDay, calendarView, taskViewManager)
            }
        })


        binding.addButton.setOnClickListener {
            val time = calendarView.selectedDates[0].timeInMillis / 1000
            startActivity(AddTaskActivity.getAddTaskScreenIntent(this, time))
        }

        binding.calenderButton.setOnClickListener {
            if (calendarView.visibility == View.GONE) {
                calendarView.visibility = View.VISIBLE
            } else calendarView.visibility = View.GONE
        }

        var tapTheCatCounter = 0
        binding.sleepingCatIV.setOnClickListener {
            tapTheCatCounter++
            if (tapTheCatCounter == 5) {
                Toast.makeText(this, getString(R.string.meow), Toast.LENGTH_SHORT).show()
                tapTheCatCounter = 0
            }
        }

    }

    private fun setupNavigationButtons(calendarView: CalendarView, taskViewManager: TaskViewManager) {
        binding.rightButton.setOnClickListener {
            val nextDay = viewModel.getNextHighlightedDay(calendarView.firstSelectedDate)
            if (nextDay != null) {
                binding.calendar.setDate(nextDay.calendar)
                updateStateOfScreen(calendarView, taskViewManager)
            } else {
                Toast.makeText(this, "No more tasks available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.leftButton.setOnClickListener {
            val previousDay = viewModel.getPreviousHighlightedDay(calendarView.firstSelectedDate)
            if (previousDay != null) {
                binding.calendar.setDate(previousDay.calendar)
                updateStateOfScreen(calendarView, taskViewManager)
            } else {
                Toast.makeText(this, "No more tasks available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStateOfScreen(
        calendarDay: CalendarDay,
        calendarView: CalendarView,
        taskViewManager: TaskViewManager
    ) {
        taskViewManager.showTasksOfTheDay(calendarDay)
        updateCurrentDate(calendarView)
        updateStateOfArrows(calendarView)
    }

    private fun updateStateOfScreen(calendarView: CalendarView, taskViewManager: TaskViewManager) {
        taskViewManager.showTasksOfTheDay(CalendarDay(calendarView.firstSelectedDate))
        updateCurrentDate(calendarView)
        updateStateOfArrows(calendarView)
    }

    private fun updateCurrentDate(calendarView: CalendarView) {
        val selectedDay =
            calendarView.firstSelectedDate.time.toInstant().atZone(ZoneId.systemDefault())
        binding.dateTV.setText(
            getString(
                R.string.dd_mm_yyyy,
                selectedDay.dayOfMonth.toString().padStart(2, '0'),
                selectedDay.monthValue.toString().padStart(2, '0'),
                selectedDay.year.toString()
            )
        )
    }

    private fun updateStateOfArrows(calendarView: CalendarView) {
        val highlightedDays = viewModel.taskListLD.value?.let { viewModel.getHighlightedDays() }
        if (highlightedDays?.let { viewModel.getNextHighlightedDay(calendarView.firstSelectedDate) } == null) {
            binding.rightButton.alpha = 0.5f
        } else {
            binding.rightButton.alpha = 1.0f
        }
        if (highlightedDays?.let { viewModel.getPreviousHighlightedDay(calendarView.firstSelectedDate) } == null) {
            binding.leftButton.alpha = 0.5f
        } else {
            binding.leftButton.alpha = 1.0f
        }
    }


}