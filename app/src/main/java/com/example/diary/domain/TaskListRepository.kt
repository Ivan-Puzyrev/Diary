package com.example.diary.domain

import androidx.lifecycle.LiveData

interface TaskListRepository {

    fun getTaskList(): LiveData<List<Task>>

    suspend fun addTask(task: Task)

    suspend fun getTaskById(id: Int): Task
}