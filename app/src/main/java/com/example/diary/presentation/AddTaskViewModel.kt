package com.example.diary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diary.domain.AddTaskUseCase
import com.example.diary.domain.GetTaskListUseCase
import com.example.diary.domain.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTaskViewModel @Inject constructor (
    private val addTaskUseCase: AddTaskUseCase,
    getTaskListUseCase: GetTaskListUseCase
    ) : ViewModel() {

    val taskListLD = getTaskListUseCase.invoke()

    private val _shouldCloseScreen = MutableLiveData(false)
    val shouldCloseScreen: LiveData<Boolean>
        get() = _shouldCloseScreen

    fun addTask(task: Task) {
        viewModelScope.launch (Dispatchers.IO) {
            addTaskUseCase.addTask(task)
            _shouldCloseScreen.postValue (true)
        }
    }
}