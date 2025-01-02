package com.example.diary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.GetTaskByIdUseCase
import com.example.diary.domain.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskDetailsViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase
) : ViewModel() {

    private val _taskLD = MutableLiveData<Task>()
    val taskLD: LiveData<Task>
        get() = _taskLD

    fun getTaskById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = getTaskByIdUseCase.getTaskById(id)
            _taskLD.postValue(task)
        }
    }
}