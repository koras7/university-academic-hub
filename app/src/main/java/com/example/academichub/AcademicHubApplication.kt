package com.example.academichub

import android.app.Application
import com.example.academichub.data.MockData
import com.example.academichub.database.AcademicRepository

class AcademicHubApplication : Application() {

    lateinit var repository: AcademicRepository

    override fun onCreate() {
        super.onCreate()

        // Initialize repository
        repository = AcademicRepository(this)

        // Pre-populate database with mock tutors if empty
        val existingTutors = repository.getAllTutors()
        if (existingTutors.isEmpty()) {
            MockData.tutors.forEach { tutor ->
                repository.insertTutor(tutor)
            }
        }
    }
}