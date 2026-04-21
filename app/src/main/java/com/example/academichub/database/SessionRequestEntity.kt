package com.example.academichub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_requests")
data class SessionRequestEntity(
    @PrimaryKey
    val id: String,
    val studentId: String,
    val studentName: String,
    val tutorId: String,
    val tutorName: String,
    val subject: String,
    val preferredTime: String,
    val note: String,
    val status: String,
    val timestamp: Long
)