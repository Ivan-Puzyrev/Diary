package com.example.diary.data

import java.time.Instant
import java.time.ZoneId
import kotlin.random.Random

object RandomTasksGenerator {

    const val MINUTE_IN_SECONDS = 60
    const val HOUR_IN_SECONDS = MINUTE_IN_SECONDS * 60
    const val DAY_IN_SECONDS = HOUR_IN_SECONDS * 24

    fun generateTasksJsonDTO(): List<TaskJsonDTO> {
        val taskJsonDTOlist = mutableListOf<TaskJsonDTO>()
        var id = 0
        for (i in 0..9) {
            val today =
                Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault())
            val startOfTheDay =
                today.toEpochSecond() - today.hour * HOUR_IN_SECONDS - (today.minute - 1) * MINUTE_IN_SECONDS
            val randomDay = (Random.nextInt(40) - 20) * DAY_IN_SECONDS
            for (j in 0..4) {
                val randomStartHour = (Random.nextInt(8) + 8) * HOUR_IN_SECONDS
                val start = startOfTheDay + randomDay + randomStartHour
                val finish = start + (Random.nextInt(4) + 3) * HOUR_IN_SECONDS
                val taskDTO = TaskJsonDTO(id, start, finish, "Task $id", "Description: $id")
                taskJsonDTOlist.add(taskDTO)
                id++
            }
        }
        return taskJsonDTOlist
    }

    fun generateTasksRoomDTO(): List<TaskRoomDTO> {
        val tasksRoomDTOlist = mutableListOf<TaskRoomDTO>()
        var id = 0
        for (i in 0..7) {
            val today =
                Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault())
            val startOfTheDay =
                today.toEpochSecond() - today.hour * HOUR_IN_SECONDS - (today.minute - 1) * MINUTE_IN_SECONDS
            val randomDay = (Random.nextInt(40) - 20) * DAY_IN_SECONDS
            for (j in 0..4) {
                val randomStartHour = (Random.nextInt(8) + 8) * HOUR_IN_SECONDS
                val start = startOfTheDay + randomDay + randomStartHour
                val finish = start + (Random.nextInt(4) + 3) * HOUR_IN_SECONDS
                val taskRoomDTO = TaskRoomDTO(id, start, finish, "Task $id", "Description: $id")
                tasksRoomDTOlist.add(taskRoomDTO)
                id++
            }
        }
        return tasksRoomDTOlist
    }
}