package com.example.academichub.database

import android.content.Context
import com.example.academichub.model.RequestStatus
import com.example.academichub.model.SessionRequest
import com.example.academichub.model.TutorProfile
import com.example.academichub.model.TutorType
import java.util.UUID

class AcademicRepository(context: Context) {

    private val database = AppDatabase.getDatabase(context)
    private val tutorDao = database.tutorDao()
    private val sessionRequestDao = database.sessionRequestDao()
    private val ratingDao = database.ratingDao()
    private val favoriteDao = database.favoriteDao()

    // TUTOR FUNCTIONS
    fun getAllTutors(): List<TutorProfile> {
        return tutorDao.getAllTutors().map { entity ->
            TutorProfile(
                id = entity.id,
                userId = entity.id,
                name = entity.name,
                type = if (entity.type == "UNIVERSITY") TutorType.UNIVERSITY else TutorType.PEER,
                subjects = entity.subjects.split(","),
                courses = entity.courses.split(","),
                availability = entity.availability,
                isPaid = entity.isPaid,
                rate = entity.rate
            )
        }
    }

    fun insertTutor(tutor: TutorProfile) {
        tutorDao.insertTutor(
            TutorEntity(
                id = tutor.id,
                name = tutor.name,
                type = tutor.type.name,
                subjects = tutor.subjects.joinToString(","),
                courses = tutor.courses.joinToString(","),
                availability = tutor.availability,
                isPaid = tutor.isPaid,
                rate = tutor.rate
            )
        )
    }

    fun getTutorById(tutorId: String): TutorProfile? {
        return tutorDao.getTutorById(tutorId)?.let { entity ->
            TutorProfile(
                id = entity.id,
                userId = entity.id,
                name = entity.name,
                type = if (entity.type == "UNIVERSITY") TutorType.UNIVERSITY else TutorType.PEER,
                subjects = entity.subjects.split(","),
                courses = entity.courses.split(","),
                availability = entity.availability,
                isPaid = entity.isPaid,
                rate = entity.rate
            )
        }
    }

    // SESSION REQUEST FUNCTIONS
    fun insertSessionRequest(request: SessionRequest) {
        sessionRequestDao.insertRequest(
            SessionRequestEntity(
                id = request.id,
                studentId = request.studentId,
                studentName = request.studentName,
                tutorId = request.tutorId,
                tutorName = request.tutorName,
                subject = request.subject,
                preferredTime = request.preferredTime,
                note = request.note,
                status = request.status.name,
                timestamp = request.timestamp
            )
        )
    }

    fun getAllSessionRequests(): List<SessionRequest> {
        return sessionRequestDao.getAllRequests().map { entity ->
            SessionRequest(
                id = entity.id,
                studentId = entity.studentId,
                studentName = entity.studentName,
                tutorId = entity.tutorId,
                tutorName = entity.tutorName,
                subject = entity.subject,
                preferredTime = entity.preferredTime,
                note = entity.note,
                status = RequestStatus.valueOf(entity.status),
                timestamp = entity.timestamp
            )
        }
    }

    fun getRequestsByStudent(studentId: String): List<SessionRequest> {
        return sessionRequestDao.getRequestsByStudent(studentId).map { entity ->
            SessionRequest(
                id = entity.id,
                studentId = entity.studentId,
                studentName = entity.studentName,
                tutorId = entity.tutorId,
                tutorName = entity.tutorName,
                subject = entity.subject,
                preferredTime = entity.preferredTime,
                note = entity.note,
                status = RequestStatus.valueOf(entity.status),
                timestamp = entity.timestamp
            )
        }
    }

    fun updateRequestStatus(requestId: String, newStatus: RequestStatus) {
        val requests = sessionRequestDao.getAllRequests()
        val request = requests.find { it.id == requestId }
        if (request != null) {
            sessionRequestDao.updateRequest(
                request.copy(status = newStatus.name)
            )
        }
    }

    // RATING FUNCTIONS
    fun insertRating(rating: RatingEntity) {
        ratingDao.insertRating(rating)
    }

    fun getRatingsForTutor(tutorId: String): List<RatingEntity> {
        return ratingDao.getRatingsForTutor(tutorId)
    }

    fun hasStudentRatedTutor(tutorId: String, studentId: String): Boolean {
        return ratingDao.hasStudentRatedTutor(tutorId, studentId)
    }

    fun getAverageRatingForTutor(tutorId: String): Float {
        val ratings = ratingDao.getRatingsForTutor(tutorId)
        if (ratings.isEmpty()) return 0f
        return ratings.map { it.rating }.average().toFloat()
    }

    // FAVORITE FUNCTIONS
    fun addFavorite(tutorId: String, studentId: String) {
        favoriteDao.insertFavorite(
            FavoriteEntity(
                id = UUID.randomUUID().toString(),
                tutorId = tutorId,
                studentId = studentId
            )
        )
    }

    fun removeFavorite(tutorId: String, studentId: String) {
        favoriteDao.deleteFavorite(tutorId, studentId)
    }

    fun isFavorite(tutorId: String, studentId: String): Boolean {
        return favoriteDao.isFavorite(tutorId, studentId)
    }

    fun getFavoriteTutors(studentId: String): List<TutorProfile> {
        return favoriteDao.getFavoritesForStudent(studentId).mapNotNull { favorite ->
            getTutorById(favorite.tutorId)
        }
    }
}