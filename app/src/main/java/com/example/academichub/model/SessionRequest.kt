package com.example.academichub.model

data class SessionRequest(
    val id: String,
    val studentId: String,
    val studentName: String,
    val tutorId: String,
    val tutorName: String,
    val subject: String,
    val preferredTime: String,
    val note: String,
    val status: RequestStatus,
    val timestamp: Long = System.currentTimeMillis()
)

enum class RequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}