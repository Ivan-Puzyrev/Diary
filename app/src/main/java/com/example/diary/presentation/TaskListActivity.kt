package com.example.diary.presentation

import android.os.Bundle
import android.util.Log
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
import java.util.Calendar
import java.util.Date
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
        updateCurrentDate(calendarView)

        val taskViewManager = TaskViewManager(this, binding.tasksCL, calendarView)

        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                calendarView.setDate(calendarDay.calendar)
                updateStateOfScreen(calendarDay, calendarView, taskViewManager)
            }
        })

        viewModel.taskListLD.observe(this) {
            taskViewManager.taskList = it
            updateStateOfScreen(calendarView, taskViewManager)
        }

        binding.rightButton.setOnClickListener {
            val selectedDate = calendarView.firstSelectedDate
            val filteredHighlightedDays =
                taskViewManager.sortedHighlightedDays.filter { it.calendar > selectedDate }.toList()
            if (filteredHighlightedDays.isNotEmpty()) {
                binding.calendar.setDate(
                    Date(filteredHighlightedDays[0].calendar.time.toInstant().toEpochMilli())
                )
                Log.d("500106", filteredHighlightedDays[0].toString())

                updateStateOfScreen(filteredHighlightedDays[0], calendarView, taskViewManager)
            } else {
                Toast.makeText(this, "No more tasks available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.leftButton.setOnClickListener {
            val selectedDate = calendarView.firstSelectedDate
            val filteredHighlightedDays =
                taskViewManager.sortedHighlightedDays.filter { it.calendar < selectedDate }.toList()
            if (filteredHighlightedDays.isNotEmpty()) {
                binding.calendar.setDate(
                    Date(
                        filteredHighlightedDays[filteredHighlightedDays.lastIndex].calendar.time.toInstant()
                            .toEpochMilli()
                    )
                )
                updateStateOfScreen(
                    filteredHighlightedDays[filteredHighlightedDays.lastIndex],
                    calendarView,
                    taskViewManager
                )
            } else {
                Toast.makeText(this, "No more tasks available", Toast.LENGTH_SHORT).show()
            }
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

        binding.addButton.setOnClickListener {
            val time = calendarView.selectedDates[0].timeInMillis / 1000
            startActivity(AddTaskActivity.getAddTaskScreenIntent(this, time))
        }
    }

    private fun updateStateOfScreen(
        calendarDay: CalendarDay,
        calendarView: CalendarView,
        taskViewManager: TaskViewManager
    ) {
        taskViewManager.showTasksOfTheDay(calendarDay)
        updateCurrentDate(calendarView)
        setupTheArrowsAlpha(calendarView, taskViewManager)
    }

    private fun updateStateOfScreen(calendarView: CalendarView, taskViewManager: TaskViewManager) {
        taskViewManager.showTasksOfTheDay(CalendarDay(calendarView.firstSelectedDate))
        taskViewManager.highlightTheDaysWithTasks()
        updateCurrentDate(calendarView)
        setupTheArrowsAlpha(calendarView, taskViewManager)
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

    private fun setupTheArrowsAlpha(calendarView: CalendarView, taskViewManager: TaskViewManager) {
        if (!isNextHighlightedDayExist(calendarView, taskViewManager)) {
            binding.rightButton.alpha = 0.5f
        } else {
            binding.rightButton.alpha = 1.0f
        }
        if (!isPreviousHighlightedDayExist(calendarView, taskViewManager)) {
            binding.leftButton.alpha = 0.5f
        } else {
            binding.leftButton.alpha = 1.0f
        }
    }

    private fun isNextHighlightedDayExist(
        calendarView: CalendarView,
        taskViewManager: TaskViewManager
    ): Boolean {
        val selectedDate = Calendar.getInstance().apply {
            set(
                calendarView.firstSelectedDate.time.year + 1900,
                calendarView.firstSelectedDate.time.month,
                calendarView.firstSelectedDate.time.date
            )
        }
        val filteredHighlightedDays =
            taskViewManager.sortedHighlightedDays.filter { it.calendar > selectedDate }
                .toList()
        return filteredHighlightedDays.isNotEmpty()
    }

    private fun isPreviousHighlightedDayExist(
        calendarView: CalendarView,
        taskViewManager: TaskViewManager
    ): Boolean {
        val selectedDate = calendarView.firstSelectedDate
        val filteredHighlightedDays =
            taskViewManager.sortedHighlightedDays.filter { it.calendar < selectedDate }
                .toList()
        return filteredHighlightedDays.isNotEmpty()
    }
}