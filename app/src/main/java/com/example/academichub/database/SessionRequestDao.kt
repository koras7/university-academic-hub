package com.example.academichub.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SessionRequestDao {

    @Insert
    fun insertRequest(request: SessionRequestEntity)

    @Query("SELECT * FROM session_requests")
    fun getAllRequests(): List<SessionRequestEntity>

    @Query("SELECT * FROM session_requests WHERE studentId = :studentId")
    fun getRequestsByStudent(studentId: String): List<SessionRequestEntity>

    @Query("SELECT * FROM session_requests WHERE tutorId = :tutorId")
    fun getRequestsByTutor(tutorId: String): List<SessionRequestEntity>

    @Update
    fun updateRequest(request: SessionRequestEntity)
}