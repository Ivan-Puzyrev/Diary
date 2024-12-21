package com.example.diary.data.json

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.diary.data.TaskListMapper
import com.example.diary.domain.Task
import com.example.diary.domain.TaskListRepository
import javax.inject.Inject

class JsonRepositoryImpl @Inject constructor(
    application: Application,
    sharedPreferences: SharedPreferences
) : TaskListRepository {

    private val jsonDB = JsonDB.getInstance(application, sharedPreferences)

    override suspend fun addTask(task: Task) {
        jsonDB.addTask(TaskListMapper.mapEntityToTaskDTO(task))
    }

    override fun getTaskList(): LiveData<List<Task>> {
        return jsonDB.taskListDtoLD.map { TaskListMapper.mapTaskListDTOtoEntityList(it) }
    }

    override suspend fun getTaskById(id: Int): Task {
        val taskList = jsonDB.taskListDtoLD.value?.filter { it.id == id } ?: throw RuntimeException(
            "There is no task with id: $id"
        )
        return TaskListMapper.mapTaskDTOtoEntity(taskList[0])
    }
}