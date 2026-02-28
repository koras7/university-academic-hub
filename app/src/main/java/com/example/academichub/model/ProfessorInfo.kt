package com.example.academichub.model

data class ProfessorInfo(
    val id: String,
    val name: String,
    val department: String,
    val courses: List<String>,
    val officeHours: String,
    val location: String,
    val email: String
)