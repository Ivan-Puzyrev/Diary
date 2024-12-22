package com.example.diary.di

import android.app.Application
import android.content.SharedPreferences
import com.example.diary.presentation.AddTaskActivity
import com.example.diary.presentation.ChooseStorageActivity
import com.example.diary.presentation.TaskDetailsActivity
import com.example.diary.presentation.TaskListActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface ApplicationComponent {

    fun inject(taskListActivity: TaskListActivity)
    fun inject(addTaskActivity: AddTaskActivity)
    fun inject(taskDetailsActivity: TaskDetailsActivity)
    fun inject(chooseStorageActivity: ChooseStorageActivity)

    @Component.Factory
    interface ApplicationComponentFactory {
        fun create(
            @BindsInstance application: Application,
            @BindsInstance sharedPreferences: SharedPreferences
        ): ApplicationComponent
    }
}