package com.example.diary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applandeo.materialcalendarview.CalendarDay
import com.example.diary.R
import com.example.diary.domain.AddTaskUseCase
import com.example.diary.domain.GetTaskListUseCase
import com.example.diary.domain.Task
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class TaskListViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    getTaskListUseCase: GetTaskListUseCase,
) : ViewModel() {

    val taskListLD = getTaskListUseCase.invoke()


    fun addTask(task: Task) {
        viewModelScope.launch {
            addTaskUseCase.addTask(task)
        }
    }

    fun getHighlightedDays(): List<CalendarDay> {
        val highlightedDays = mutableSetOf<CalendarDay>()
        taskListLD.value?.forEach {
            val day = Calendar.getInstance()
            day.set(it.dateStart.year, it.dateStart.month.value - 1, it.dateStart.dayOfMonth)
            day.set(Calendar.MILLISECOND, 0)
            val calendarDay = CalendarDay(day)
            calendarDay.labelColor = R.color.white
            calendarDay.backgroundResource = R.drawable.calender_highlight
            highlightedDays.add(calendarDay)
        }
        return highlightedDays.sortedBy { it.calendar.time }.toList()
    }

    fun getNextHighlightedDay(calendar: Calendar): CalendarDay? {
        val selectedDate = Calendar.getInstance().apply {
            set(
                calendar.time.year + 1900,
                calendar.time.month,
                calendar.time.date
            )
        }
        val nextHighlightedDays = getHighlightedDays().filter { it.calendar > selectedDate }
        return if (nextHighlightedDays.isNotEmpty()) {
            nextHighlightedDays[0]
        } else null
    }

    fun getPreviousHighlightedDay(calendar: Calendar): CalendarDay? {
        val previousHighlightedDays = getHighlightedDays().filter { it.calendar < calendar }.toList()
        return if (previousHighlightedDays.isNotEmpty()) {
            previousHighlightedDays[previousHighlightedDays.lastIndex]
        } else null
    }

}