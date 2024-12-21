package com.example.diary.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskListDAO {

    @Query("SELECT * FROM task_list")
    fun getTaskList(): LiveData<List<TaskRoomDTO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(taskRoomDTO: TaskRoomDTO)

    @Query("SELECT * FROM task_list WHERE id=:taskId LIMIT 1")
    suspend fun getTask(taskId: Int): TaskRoomDTO
}