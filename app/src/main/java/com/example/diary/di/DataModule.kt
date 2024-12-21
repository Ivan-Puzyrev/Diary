package com.example.diary.di

import android.app.Application
import android.content.SharedPreferences
import com.example.diary.data.room.AppRoomDatabase
import com.example.diary.data.json.JsonRepositoryImpl
import com.example.diary.data.room.RoomRepositoryImpl
import com.example.diary.data.room.TaskListDAO
import com.example.diary.domain.TaskListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @RoomQualifier
    @Binds
    fun bindRoomRepository(impl: RoomRepositoryImpl): TaskListRepository

    companion object {
        private const val REPOSITORY_TYPE_KEY = "repository_type"
        private const val JSON_KEY = "Json"

        @JsonQualifier
        @Provides
        fun provideJsonRepository(
            application: Application,
            sharedPreferences: SharedPreferences
        ): TaskListRepository {
            return JsonRepositoryImpl(application, sharedPreferences)
        }

        @Provides
        fun provideTaskListDAO(
            application: Application,
            sharedPreferences: SharedPreferences
        ): TaskListDAO {
            return AppRoomDatabase.getInstance(application, sharedPreferences).taskListDAO()
        }

        @Provides
        fun provideSelectedRepository(
            sharedPreferences: SharedPreferences,
            @JsonQualifier jsonRepository: TaskListRepository,
            @RoomQualifier roomRepository: TaskListRepository
        ): TaskListRepository {
            val repositoryType = sharedPreferences.getString(REPOSITORY_TYPE_KEY, JSON_KEY)

            return when (repositoryType) {
                "Json" -> jsonRepository
                "Room" -> roomRepository
                else -> throw RuntimeException("Unknown repository type")
            }
        }
    }
}