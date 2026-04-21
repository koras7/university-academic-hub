package com.example.academichub.model

data class TutoringSession(
    val id: String,
    val tutorId: String,
    val tutorName: String,
    val subject: String,
    val description: String,
    val availableTime: String,
    val maxStudents: Int,
    val isPaid: Boolean,
    val rate: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)