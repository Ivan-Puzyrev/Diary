package com.example.diary.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class JsonDB @Inject constructor(
    private val application: Application,
    private val sharedPreferences: SharedPreferences
) {

    init {
        CoroutineScope(Dispatchers.IO).launch { loadTasksFromFile() }
    }

    private val taskListDTO = mutableListOf<TaskJsonDTO>()
    private val _taskListDtoLD = MutableLiveData<List<TaskJsonDTO>>()
    val taskListDtoLD: LiveData<List<TaskJsonDTO>>
        get() = _taskListDtoLD

    suspend fun addTask(taskDTO: TaskJsonDTO) {
        taskListDTO.add(taskDTO)
        saveTasksToFile()
        _taskListDtoLD.postValue(taskListDTO)
    }

    private suspend fun saveTasksToFile() {
        val json = Gson().toJson(taskListDTO)
        withContext(Dispatchers.IO) {
            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        }
    }

    private suspend fun loadTasksFromFile() {
        val file = File(application.filesDir, FILE_NAME)
        val gson = Gson()
        val type = object : TypeToken<List<TaskJsonDTO>>() {}.type

        withContext(Dispatchers.IO) {
            if (file.exists()) {
                val fileContent = file.readText()
                gson.fromJson<List<TaskJsonDTO>?>(fileContent, type).forEach {
                    taskListDTO.add(it)
                }
                _taskListDtoLD.postValue(taskListDTO)
            } else {
                if (sharedPreferences.getBoolean(TASK_GENERATOR_KEY, false)) {
                    RandomTasksGenerator.generateTasksJsonDTO().forEach {
                        taskListDTO.add(it)
                    }
                    saveTasksToFile()
                    _taskListDtoLD.postValue(taskListDTO)
                }
                saveTasksToFile()
                _taskListDtoLD.postValue(taskListDTO)
            }
        }
    }

    companion object {
        private const val TASK_GENERATOR_KEY = "task_generator"
        private const val FILE_NAME = "Task_data.json"
        private var INSTANCE: JsonDB? = null
        private val LOCK = Any()

        fun getInstance(application: Application, sharedPreferences: SharedPreferences): JsonDB {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val db = JsonDB(application, sharedPreferences)
                INSTANCE = db
                return db
            }
        }
    }
}