package com.example.diary.presentation

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var calendarView: CalendarView
    private lateinit var taskViewManager: TaskViewManager
    private val component by lazy { (application as DiaryApp).component }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[TaskListViewModel::class]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calendarView = binding.calendar.apply { setFirstDayOfWeek(CalendarWeekDay.MONDAY) }
        taskViewManager = TaskViewManager(this, binding.tasksCL)

        observeViewModel()
        setupClickListeners()
    }

    private fun observeViewModel() {
        with(viewModel) {
            taskListLD.observe(this@TaskListActivity) {
                taskViewManager.taskList = it
                viewModel.highlightDays()
                updateStateOfScreen()
            }

            highlightedDaysLD.observe(this@TaskListActivity) {
                calendarView.setCalendarDays(it)
            }

            arrowsStatusLD.observe(this@TaskListActivity) {
                if (it.first) {
                    binding.leftButton.alpha = 1.0f
                } else binding.leftButton.alpha = 0.5f
                if (it.second) {
                    binding.rightButton.alpha = 1.0f
                } else binding.rightButton.alpha = 0.5f
            }

            selectedHighlightedDayLD.observe(this@TaskListActivity) {
                if (it != null) {
                    calendarView.setDate(it.calendar)
                    updateStateOfScreen()
                    shortVibration()
                } else {
                    showNoMoreTasksToast()
                }
            }

            noMoreTasksToast.observe(this@TaskListActivity) {
                if (it) {
                    Toast.makeText(
                        this@TaskListActivity,
                        R.string.no_more_tasks_available,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            catToast.observe(this@TaskListActivity) {
                if (it) {
                    Toast.makeText(this@TaskListActivity, R.string.meow, Toast.LENGTH_SHORT).show()
                    shortVibration()
                }
            }
        }
    }

    private fun setupClickListeners() {
        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                calendarView.setDate(calendarDay.calendar)
                updateStateOfScreen()
            }
        })

        with(binding) {
            addButton.setOnClickListener {
                val time = calendarView.selectedDates[0].timeInMillis / 1000
                startActivity(AddTaskActivity.getAddTaskScreenIntent(this@TaskListActivity, time))
            }

            calenderButton.setOnClickListener {
                if (calendarView.visibility == View.GONE) {
                    calendarView.visibility = View.VISIBLE
                } else calendarView.visibility = View.GONE
            }

            sleepingCatIV.setOnClickListener { viewModel.tapTheCat() }

            rightButton.setOnClickListener { viewModel.moveToNextHighlightedDay(calendarView.firstSelectedDate) }

            leftButton.setOnClickListener { viewModel.moveToPreviousHighlightedDay(calendarView.firstSelectedDate) }
        }
    }

    private fun updateStateOfScreen() {
        updateCurrentDate()
        taskViewManager.showTasksOfTheDay(CalendarDay(calendarView.firstSelectedDate))
        viewModel.updateStateOfArrows(calendarView.firstSelectedDate)
    }

    private fun updateCurrentDate() {
        val selectedDay =
            calendarView.firstSelectedDate.time.toInstant().atZone(ZoneId.systemDefault())
        binding.dateTV.text = getString(
            R.string.dd_mm_yyyy,
            selectedDay.dayOfMonth.toString().padStart(2, '0'),
            selectedDay.monthValue.toString().padStart(2, '0'),
            selectedDay.year.toString()
        )
    }

    private fun shortVibration() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(70, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}