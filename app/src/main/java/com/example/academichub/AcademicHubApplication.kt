package com.example.academichub

import android.app.Application
import com.example.academichub.data.MockData
import com.example.academichub.database.AcademicRepository
import com.example.academichub.model.UserRole
import com.example.academichub.util.NotificationHelper

class AcademicHubApplication : Application() {

    lateinit var repository: AcademicRepository

    override fun onCreate() {
        super.onCreate()

        NotificationHelper.createChannel(this)

        // Initialize repository
        repository = AcademicRepository(this)

        // Pre-populate database with mock tutors if empty
        val existingTutors = repository.getAllTutors()
        if (existingTutors.isEmpty()) {
            MockData.tutors.forEach { tutor ->
                repository.insertTutor(tutor)
            }
        }

        // Pre-populate demo users so login works out of the box.
        // registerUser is a no-op if the email already exists.
        repository.registerUser(
            name = "Test Student",
            email = "student@university.edu",
            password = "student123",
            role = UserRole.STUDENT.name
        )
        repository.registerUser(
            name = "Test Tutor",
            email = "tutor@university.edu",
            password = "tutor123",
            role = UserRole.PEER_TUTOR.name
        )
        repository.registerUser(
            name = "Test Professor",
            email = "prof@university.edu",
            password = "prof123",
            role = UserRole.PROFESSOR.name
        )
    }
}
