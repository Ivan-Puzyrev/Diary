package com.example.diary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.GetTaskByIdUseCase
import com.example.diary.domain.Task
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskDetailsViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase
) : ViewModel() {

    private val _task = MutableLiveData<Task>()
    val task: LiveData<Task>
        get() = _task

    fun getTaskById(id: Int) {
        viewModelScope.launch {
            val task = getTaskByIdUseCase.getTaskById(id)
            _task.value = task
        }
    }
}