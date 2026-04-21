package com.example.academichub.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TutorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTutor(tutor: TutorEntity)

    @Query("SELECT * FROM tutors")
    fun getAllTutors(): List<TutorEntity>

    @Query("SELECT * FROM tutors WHERE id = :tutorId")
    fun getTutorById(tutorId: String): TutorEntity?
}