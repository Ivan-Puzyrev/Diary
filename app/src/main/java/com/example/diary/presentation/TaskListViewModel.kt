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
    val highlightedDays: List<CalendarDay>
        get() {
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

    fun addTask(task: Task) {
        viewModelScope.launch {
            addTaskUseCase.addTask(task)
        }
    }

    fun getNextHighlightedDay(calendar: Calendar): CalendarDay? {
        val selectedDate = Calendar.getInstance().apply {
            set(
                calendar.time.year + 1900,
                calendar.time.month,
                calendar.time.date
            )
        }
        return highlightedDays.find { it.calendar > selectedDate }
    }

    fun getPreviousHighlightedDay(calendar: Calendar): CalendarDay? {
        return highlightedDays.sortedByDescending { it.calendar }.find { it.calendar < calendar }
    }
}