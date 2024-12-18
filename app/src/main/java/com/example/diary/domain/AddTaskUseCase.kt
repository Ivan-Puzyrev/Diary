package com.example.diary.domain

import javax.inject.Inject

class AddTaskUseCase @Inject constructor (private val taskListRepository: TaskListRepository) {

    suspend fun addTask(task: Task) {
        taskListRepository.addTask(task)
    }
}