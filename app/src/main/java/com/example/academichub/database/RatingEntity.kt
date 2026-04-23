package com.example.academichub.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ratings")
data class RatingEntity(
    @PrimaryKey
    val id: String,
    val tutorId: String,
    val studentId: String,
    val rating: Float,
    val comment: String
)
