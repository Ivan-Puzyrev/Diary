package com.example.diary.data.room

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.diary.data.RandomTasksGenerator

@Database(entities = [TaskRoomDTO::class], version = 1, exportSchema = false)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun taskListDAO(): TaskListDAO

    companion object {
        private var INSTANCE: AppRoomDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "task_list.db"
        private const val TASK_GENERATOR_KEY = "task_generator"

        fun getInstance(
            application: Application,
            sharedPreferences: SharedPreferences
        ): AppRoomDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
                val dbBuilder = Room.databaseBuilder(
                    application,
                    AppRoomDatabase::class.java,
                    DB_NAME
                )
                if (sharedPreferences.getBoolean(TASK_GENERATOR_KEY, false)) {
                    dbBuilder.addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            RandomTasksGenerator.generateTasksRoomDTO().forEach {
                                db.execSQL("INSERT INTO task_list (id, dateStart, dateFinish, name, description) VALUES (${it.id}, ${it.dateStart}, ${it.dateFinish}, '${it.name}', '${it.description}')")
                            }
                        }
                    })
                }
                val db = dbBuilder.build()
                INSTANCE = db
                return db
            }
        }
    }
}