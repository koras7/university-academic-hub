package com.example.academichub.model

data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole
)

enum class UserRole {
    STUDENT,
    PEER_TUTOR,
    UNIVERSITY_TUTOR,
    PROFESSOR
}