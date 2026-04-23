package com.example.academichub.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RatingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRating(rating: RatingEntity)

    @Query("SELECT * FROM ratings WHERE tutorId = :tutorId")
    fun getRatingsForTutor(tutorId: String): List<RatingEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM ratings WHERE tutorId = :tutorId AND studentId = :studentId)")
    fun hasStudentRatedTutor(tutorId: String, studentId: String): Boolean
}
