package com.example.diary.presentation

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.diary.domain.Task
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@RunWith(MockitoJUnitRunner::class)
class TaskViewManagerTest {

    @Mock
    private lateinit var mockContext: Context
    private lateinit var taskViewManager: TaskViewManager
    private var startOfTheDay = 0L

    @Before
    fun setUp() {
        val dummyConstraintLayout = ConstraintLayout(mockContext)
        taskViewManager = TaskViewManager(mockContext, dummyConstraintLayout)
        taskViewManager.viewColumns.add(mutableListOf(View(mockContext)))
        val currentTime = ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(System.currentTimeMillis() / 1000),
            ZoneId.systemDefault()
        )
        startOfTheDay = currentTime.toEpochSecond() - currentTime.hour * ONE_HOUR_IN_SECONDS
        println(startOfTheDay.toString())
        val dateStart =
            ZonedDateTime.ofInstant(Instant.ofEpochSecond(startOfTheDay), ZoneId.systemDefault())
        val dateFinish = ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(startOfTheDay + TWO_HOURS_IN_SECONDS),
            ZoneId.systemDefault()
        )
        taskViewManager.taskColumns.add(mutableListOf(Task(0, dateStart, dateFinish, "", "")))
    }

    @Test
    fun `A task with intersected time should be added to a new column (#1)`() {
        val dateStart = ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(startOfTheDay + ONE_HOUR_IN_SECONDS),
            ZoneId.systemDefault()
        )
        val dateFinish = ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(startOfTheDay + THREE_HOURS_IN_SECONDS),
            ZoneId.systemDefault()
        )
        val freeColumn = taskViewManager.findFreeColumn(Task(0, dateStart, dateFinish, "", ""))
        assertEquals(1, freeColumn)
    }

    @Test
    fun `A task without intersected time should be added to the existing column (#0)`() {
        val dateStart = ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(startOfTheDay + THREE_HOURS_IN_SECONDS),
            ZoneId.systemDefault()
        )
        val dateFinish = ZonedDateTime.ofInstant(
            Instant.ofEpochSecond(startOfTheDay + FOUR_HOURS_IN_SECONDS),
            ZoneId.systemDefault()
        )
        val freeColumn = taskViewManager.findFreeColumn(Task(0, dateStart, dateFinish, "", ""))
        assertEquals(0, freeColumn)
    }

    companion object {
        private const val ONE_HOUR_IN_SECONDS = 3600
        private const val TWO_HOURS_IN_SECONDS = ONE_HOUR_IN_SECONDS * 2
        private const val THREE_HOURS_IN_SECONDS = ONE_HOUR_IN_SECONDS * 3
        private const val FOUR_HOURS_IN_SECONDS = ONE_HOUR_IN_SECONDS * 4
    }
}