package com.example.diary.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_list")
data class TaskRoomDTO (
    @PrimaryKey
    val id: Int,
    val dateStart: Long,
    val dateFinish: Long,
    val name: String,
    val description: String
)