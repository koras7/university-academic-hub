package com.example.academichub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tutors")
data class TutorEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val type: String,
    val subjects: String,
    val courses: String,
    val availability: String,
    val isPaid: Boolean,
    val rate: String?
)