package com.example.diary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.example.diary.R
import com.example.diary.domain.GetTaskListUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

class TaskListViewModel @Inject constructor(getTaskListUseCase: GetTaskListUseCase) : ViewModel() {

    val taskListLD = getTaskListUseCase.invoke()

    private val _highlightedDaysLD = MutableLiveData<List<CalendarDay>>()
    val highlightedDaysLD: LiveData<List<CalendarDay>>
        get() = _highlightedDaysLD

    private val _selectedHighlightedDayLD = MutableLiveData<CalendarDay?>()
    val selectedHighlightedDayLD: LiveData<CalendarDay?>
        get() = _selectedHighlightedDayLD

    private val _arrowsStatusLD = MutableLiveData<Pair<Boolean, Boolean>>()
    val arrowsStatusLD: LiveData<Pair<Boolean, Boolean>>
        get() = _arrowsStatusLD

    private val _noMoreTasksToast = MutableLiveData(false)
    val noMoreTasksToast: LiveData<Boolean>
        get() = _noMoreTasksToast

    private val _catToast = MutableLiveData(false)
    val catToast: LiveData<Boolean>
        get() = _catToast

    private var isToastVisible = true
    private var tapTheCatCounter = 0

    fun updateStateOfArrows(calendarView: CalendarView) {
        val isPreviousDayExist = checkPreviousHighlightedDay(calendarView.firstSelectedDate) != null
        val isNextDayExist = checkNextHighlightedDay(calendarView.firstSelectedDate) != null
        _arrowsStatusLD.value = Pair(isPreviousDayExist, isNextDayExist)
    }

    fun highlightDays() {
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
        _highlightedDaysLD.value = highlightedDays.sortedBy { it.calendar.time }.toList()
    }

    fun moveToPreviousHighlightedDay(calendar: Calendar) {
        _selectedHighlightedDayLD.value = checkPreviousHighlightedDay(calendar)
    }

    fun moveToNextHighlightedDay(calendar: Calendar) {
        _selectedHighlightedDayLD.value = checkNextHighlightedDay(calendar)
    }

    private fun checkPreviousHighlightedDay(calendar: Calendar): CalendarDay? {
        return _highlightedDaysLD.value?.sortedByDescending { it.calendar }
            ?.find { it.calendar < calendar }
    }

    private fun checkNextHighlightedDay(calendar: Calendar): CalendarDay? {
        val selectedDate = Calendar.getInstance().apply {
            set(
                calendar.time.year + 1900,
                calendar.time.month,
                calendar.time.date
            )
        }
        return _highlightedDaysLD.value?.find { it.calendar > selectedDate }
    }

    fun tapTheCat() {
        if (isToastVisible) {
            tapTheCatCounter++
            if (tapTheCatCounter == 5) {
                tapTheCatCounter = 0
                isToastVisible = false
                _catToast.value = true
                viewModelScope.launch {
                    delay(3000)
                    isToastVisible = true
                }
            }
        }
    }

    fun showNoMoreTasksToast() {
        if (isToastVisible) {
            isToastVisible = false
            _noMoreTasksToast.value = true
            viewModelScope.launch {
                delay(3000)
                isToastVisible = true
            }
        }
    }
}