package com.example.academichub.model

data class TutorProfile(
    val id: String,
    val userId: String,
    val name: String,
    val type: TutorType,
    val subjects: List<String>,
    val courses: List<String>,
    val availability: String,
    val isPaid: Boolean = false,
    val rate: String? = null
)

enum class TutorType {
    PEER,
    UNIVERSITY
}