package com.example.diary.di

import androidx.lifecycle.ViewModel
import com.example.diary.presentation.AddTaskViewModel
import com.example.diary.presentation.TaskDetailsViewModel
import com.example.diary.presentation.TaskListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(TaskListViewModel::class)
    fun bindMainViewModel (impl: TaskListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTaskViewModel::class)
    fun bindAddTaskViewModel (addTaskViewModel: AddTaskViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskDetailsViewModel::class)
    fun bindTaskDetailsViewModel (taskDetailsViewModel: TaskDetailsViewModel): ViewModel

}