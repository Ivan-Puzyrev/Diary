package com.example.diary.data

import com.google.gson.annotations.SerializedName

data class TaskJsonDTO (
    @SerializedName("id")
    val id: Int,
    @SerializedName("date_start")
    val dateStart: Long,
    @SerializedName("date_finish")
    val dateFinish: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String
)