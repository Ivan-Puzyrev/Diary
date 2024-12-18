package com.example.diary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.AddTaskUseCase
import com.example.diary.domain.GetTaskListUseCase
import com.example.diary.domain.Task
import kotlinx.coroutines.launch
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

}