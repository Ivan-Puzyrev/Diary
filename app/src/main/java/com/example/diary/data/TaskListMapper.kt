package com.example.diary.data

import com.example.diary.domain.Task
import java.time.Instant
import java.time.ZoneId

object TaskListMapper {

    fun mapEntityToTaskDTO(task: Task): TaskJsonDTO {
        return TaskJsonDTO(
            task.id,
            task.dateStart.toEpochSecond(),
            task.dateFinish.toEpochSecond(),
            task.name,
            task.description
        )
    }

    fun mapTaskDTOtoEntity(taskDTO: TaskJsonDTO): Task {
        val dateStart = Instant.ofEpochSecond(taskDTO.dateStart).atZone(ZoneId.systemDefault())
        val dateFinish = Instant.ofEpochSecond(taskDTO.dateFinish).atZone(ZoneId.systemDefault())

        return Task(
            taskDTO.id,
            dateStart,
            dateFinish,
            taskDTO.name,
            taskDTO.description
        )
    }

    fun mapTaskListDTOtoEntityList(taskListDTO: List<TaskJsonDTO>): List<Task> {
        return taskListDTO.map { mapTaskDTOtoEntity(it) }
    }

    fun mapEntityToTaskRoomDTO(task: Task): TaskRoomDTO {
        return TaskRoomDTO(
            task.id,
            task.dateStart.toEpochSecond(),
            task.dateFinish.toEpochSecond(),
            task.name,
            task.description
        )
    }

    fun mapTaskRoomDTOtoEntity(taskRoomDTO: TaskRoomDTO): Task {
        val dateStart = Instant.ofEpochSecond(taskRoomDTO.dateStart).atZone(ZoneId.systemDefault())
        val dateFinish =
            Instant.ofEpochSecond(taskRoomDTO.dateFinish).atZone(ZoneId.systemDefault())

        return Task(
            taskRoomDTO.id,
            dateStart,
            dateFinish,
            taskRoomDTO.name,
            taskRoomDTO.description
        )
    }

    fun mapTaskListRoomDTOtoEntityList(taskListRoomDTO: List<TaskRoomDTO>): List<Task> {
        return taskListRoomDTO.map { mapTaskRoomDTOtoEntity(it) }
    }
}