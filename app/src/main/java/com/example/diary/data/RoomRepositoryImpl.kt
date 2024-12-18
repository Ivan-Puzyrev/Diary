package com.example.diary.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.diary.domain.Task
import com.example.diary.domain.TaskListRepository
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(private val taskListDAO: TaskListDAO) : TaskListRepository {

    override suspend fun addTask(task: Task) {
        taskListDAO.addTask(TaskListMapper.mapEntityToTaskRoomDTO(task))
    }

    override suspend fun getTaskById(id: Int): Task {
        return TaskListMapper.mapTaskRoomDTOtoEntity(taskListDAO.getTask(id))
    }

    override fun getTaskList(): LiveData<List<Task>> {
        return taskListDAO.getTaskList().map { TaskListMapper.mapTaskListRoomDTOtoEntityList(it) }
    }
}