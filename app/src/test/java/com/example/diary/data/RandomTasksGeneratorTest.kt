package com.example.diary.data

import com.applandeo.materialcalendarview.CalendarDay
import com.example.diary.domain.Task
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.ZoneId
import java.util.Calendar

class RandomTasksGeneratorTest {

    private lateinit var taskList: List<Task>
    private lateinit var uniqueDays: MutableSet<CalendarDay>

    @Before
    fun setUp() {
        taskList = RandomTasksGenerator.generateTasksJsonDTO()
            .map { TaskListMapper.mapTaskDTOtoEntity(it) }
        uniqueDays = mutableSetOf()
        taskList.forEach {
            val day = Calendar.getInstance()
            day.set(it.dateStart.year, it.dateStart.month.value - 1, it.dateStart.dayOfMonth)
            day.set(Calendar.MILLISECOND, 0)
            val calendarDay = CalendarDay(day)
            uniqueDays.add(calendarDay)
        }
    }

    @Test
    fun `There should be generated 8 unique days with tasks`() {
        assertEquals(8, uniqueDays.size)
    }

    @Test
    fun `Each day should contain 5 tasks`() {
        val taskCountForEachDay = mutableListOf<Int>()
        uniqueDays.forEach {
            val uniqueDayTime = it.calendar.time.toInstant().atZone(ZoneId.systemDefault())
            val count = taskList.count {
                it.dateStart.year == uniqueDayTime.year &&
                it.dateStart.month == uniqueDayTime.month &&
                it.dateStart.dayOfMonth == uniqueDayTime.dayOfMonth
            }
            taskCountForEachDay.add(count)
        }
        assertEquals(8, taskCountForEachDay.count { it == 5 })
    }
}