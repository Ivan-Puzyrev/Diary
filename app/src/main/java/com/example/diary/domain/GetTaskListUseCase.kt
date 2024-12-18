package com.example.diary.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetTaskListUseCase @Inject constructor(private val taskListRepository: TaskListRepository) {

    operator fun invoke(): LiveData<List<Task>> = taskListRepository.getTaskList()

}