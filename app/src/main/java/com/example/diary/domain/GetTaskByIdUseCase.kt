package com.example.diary.domain

import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor (private val taskListRepository: TaskListRepository) {

    suspend fun getTaskById(id: Int): Task {
        return taskListRepository.getTaskById(id)
    }
}