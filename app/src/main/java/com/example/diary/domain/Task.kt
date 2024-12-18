package com.example.diary.domain

import java.time.ZonedDateTime

data class Task(
    val id: Int,
    val dateStart: ZonedDateTime,
    val dateFinish: ZonedDateTime,
    val name: String,
    val description: String
)