package com.example.diary.presentation

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
import androidx.constraintlayout.widget.ConstraintSet
import com.applandeo.materialcalendarview.CalendarDay
import com.example.diary.R
import com.example.diary.databinding.TaskBinding
import com.example.diary.domain.Task
import java.time.ZoneId

class TaskViewManager(
    private val context: Context,
    private val constraintLayout: ConstraintLayout,
    private val calendarView: com.applandeo.materialcalendarview.CalendarView
) {
    var sortedHighlightedDays = listOf<CalendarDay>()
    var taskList: List<Task> = listOf()

    private var hoursTV: List<View>
    private val viewColumns: MutableList<MutableList<View>> = mutableListOf()
    private val taskColumns: MutableList<MutableList<Task>> = mutableListOf()
    private val sleepingCatIV = constraintLayout.getViewById(R.id.sleepingCatIV)
    private val sleepingCatTV = constraintLayout.getViewById(R.id.noTasksTV)

    init {
        hoursTV = initHoursTV()
    }

    private fun addTaskToConstraintLayout(task: Task) {
        val numberOfColumn = findFreeColumn(task)

        val textViewBinding =
            TaskBinding.inflate(LayoutInflater.from(context), constraintLayout, false)
        val textView = textViewBinding.root
        textView.id = View.generateViewId()

        textView.background = context.getDrawable(R.drawable.task_border)
        val shape = textView.background as GradientDrawable
        val activity = context as AppCompatActivity
        val backgroundColor = (activity.window.decorView.background as? ColorDrawable)?.color
        if (backgroundColor != null) {
            shape.setStroke(3, backgroundColor)
        }

        textViewBinding.timeTV.text = context.getString(
            R.string.from_to,
            task.dateStart.hour.toString(),
            task.dateFinish.hour.toString()
        )
        textViewBinding.taskTV.setText(task.name)

        textView.setOnClickListener {
            activity.startActivity(TaskDetailsActivity.getTaskDetailsIntent(activity, task.id))
        }

        if (numberOfColumn == -1 || numberOfColumn == 0) {
            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                startToEnd = hoursTV.get(task.dateStart.hour).id
                if (viewColumns.size < 2) {
                    endToEnd = LayoutParams.PARENT_ID
                } else {
                    endToStart = viewColumns.get(numberOfColumn + 1).get(0).id
                }
                marginStart = 10
                topToTop = hoursTV.get(task.dateStart.hour).id
                if (task.dateFinish.hour == 0) {
                    bottomToBottom = hoursTV.get(23).id
                } else {
                    bottomToTop = hoursTV.get(task.dateFinish.hour).id
                }
                height = LayoutParams.MATCH_CONSTRAINT
                width = LayoutParams.MATCH_CONSTRAINT
            }
            taskColumns.get(0).add(task)
            viewColumns.get(0).add(textView)
            constraintLayout.addView(textView, params)
        } else {
            val params = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                startToEnd = viewColumns.get(numberOfColumn - 1).get(0).id
                if (numberOfColumn == viewColumns.size - 1) {
                    endToEnd = LayoutParams.PARENT_ID
                } else {
                    endToStart = viewColumns.get(numberOfColumn + 1).get(0).id
                }
                topToTop = hoursTV.get(task.dateStart.hour).id
                if (task.dateFinish.hour == 0) {
                    bottomToBottom = hoursTV.get(23).id
                } else {
                    bottomToTop = hoursTV.get(task.dateFinish.hour).id
                }
                height = LayoutParams.MATCH_CONSTRAINT
                width = LayoutParams.MATCH_CONSTRAINT
            }

            taskColumns.get(numberOfColumn).add(task)
            viewColumns.get(numberOfColumn).add(textView)
            constraintLayout.addView(textView, params)

            if (numberOfColumn == viewColumns.size - 1) {
                for (view in viewColumns.get(viewColumns.lastIndex - 1)) {
                    updateConstraint(
                        constraintLayout,
                        view.id,
                        LayoutParams.END,
                        textView.id,
                        LayoutParams.START
                    )
                }
            }
        }
    }

    private fun findFreeColumn(task: Task): Int {
        if (viewColumns.isEmpty()) {
            taskColumns.add(mutableListOf())
            viewColumns.add(mutableListOf())
            return -1
        } else {
            for (i in 0..taskColumns.size - 1) {
                val column = taskColumns.get(i)
                for (j in 0..column.size - 1) {
                    val columnTask = column.get(j)
                    if ((task.dateStart.hour >= columnTask.dateFinish.hour && columnTask.dateFinish.hour != 0) ||
                        (task.dateStart.hour <= columnTask.dateStart.hour &&
                                task.dateFinish.hour <= columnTask.dateStart.hour && task.dateFinish.hour != 0)
                    ) {
                        if (j == column.size - 1) {
                            return i
                        }
                    } else {
                        break
                    }
                }

            }
            taskColumns.add(mutableListOf())
            viewColumns.add(mutableListOf())
            return taskColumns.size - 1
        }
    }

    private fun updateConstraint(
        constraintLayout: ConstraintLayout,
        viewId: Int,
        side: Int,
        targetViewId: Int,
        targetSide: Int,
        margin: Int = 0
    ) {
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.connect(
            viewId,
            side,
            targetViewId,
            targetSide,
            margin
        )
        constraintSet.applyTo(constraintLayout)
    }

    private fun initHoursTV(): List<View> {
        val parent = constraintLayout
        val textViews = mutableListOf<TextView>()
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            if (child is TextView && !child.text.equals(context.getString(R.string.no_tasks_for_this_day))) {
                textViews.add(child)
            }
        }
        Log.d("init", textViews.size.toString())
        return textViews.toList()
    }

    private fun showHoursTV(taskList: List<Task>) {
        val minStartHour = taskList.minBy { it.dateStart.hour }.dateStart.hour
        val isMaxFinishZero = taskList.minBy { it.dateFinish.hour }.dateFinish.hour.equals(0)
        val maxFinishHour = if (isMaxFinishZero) {
            24
        } else {
            taskList.maxBy { it.dateFinish.hour }.dateFinish.hour
        }

        for (i in minStartHour..maxFinishHour - 1) {
            hoursTV.get(i).visibility = View.VISIBLE
        }
    }

    fun showTasksOfTheDay(calendarDay: CalendarDay) {
        sleepingCatIV.visibility = View.GONE
        sleepingCatTV.visibility = View.GONE
        val localDate = calendarDay.calendar.time.toInstant().atZone(ZoneId.systemDefault())
        hoursTV.forEach {
            it.visibility = View.GONE
        }

        viewColumns.flatMap { it }.forEach {
            constraintLayout.removeView(it)
        }
        viewColumns.clear()
        taskColumns.clear()
        val taskListOfTheDay = taskList.filter {
            it.dateStart.year == localDate.year &&
                    it.dateStart.month == localDate.month &&
                    it.dateStart.dayOfMonth == localDate.dayOfMonth
        }
        if (!taskListOfTheDay.isEmpty()) {
            taskListOfTheDay.forEach {
                addTaskToConstraintLayout(it)
                Log.d("500106", "${it.name} from ${it.dateStart.hour} to ${it.dateFinish.hour}")
            }
            showHoursTV(taskListOfTheDay)
        } else {
            sleepingCatIV.visibility = View.VISIBLE
            sleepingCatTV.visibility = View.VISIBLE
        }


    }

    fun highlightTheDaysWithTasks() {
        val highlightedDays = mutableSetOf<CalendarDay>()
        taskList.forEach {
            val day = java.util.Calendar.getInstance()
            day.set(it.dateStart.year, it.dateStart.month.value - 1, it.dateStart.dayOfMonth)
            val calendarDay = CalendarDay(day)
            calendarDay.labelColor = R.color.white
            calendarDay.backgroundResource = R.drawable.calender_highlight
            highlightedDays.add(calendarDay)
        }
        sortedHighlightedDays = highlightedDays.sortedBy { it.calendar.time }.toList()
        calendarView.setCalendarDays(highlightedDays.toList())
    }
}