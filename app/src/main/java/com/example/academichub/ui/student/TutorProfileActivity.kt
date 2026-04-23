package com.example.academichub.ui.student

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.database.AcademicRepository
import com.example.academichub.database.RatingEntity
import com.example.academichub.model.TutorType
import com.example.academichub.model.UserRole
import java.util.Locale
import java.util.UUID
import java.util.concurrent.Executors

class TutorProfileActivity : AppCompatActivity() {

    private lateinit var profileName: TextView
    private lateinit var profileType: TextView
    private lateinit var profileSubjects: TextView
    private lateinit var profileCourses: TextView
    private lateinit var profileAvailability: TextView
    private lateinit var profileRate: TextView
    private lateinit var profileRatingSummary: TextView
    private lateinit var requestSessionButton: Button
    private lateinit var rateButton: TextView
    private lateinit var favoriteButton: TextView

    private lateinit var repository: AcademicRepository
    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    private var tutorId: String? = null
    private var isFavorited: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor_profile)

        profileName = findViewById(R.id.profileName)
        profileType = findViewById(R.id.profileType)
        profileSubjects = findViewById(R.id.profileSubjects)
        profileCourses = findViewById(R.id.profileCourses)
        profileAvailability = findViewById(R.id.profileAvailability)
        profileRate = findViewById(R.id.profileRate)
        profileRatingSummary = findViewById(R.id.profileRatingSummary)
        requestSessionButton = findViewById(R.id.requestSessionButton)
        rateButton = findViewById(R.id.rateButton)
        favoriteButton = findViewById(R.id.favoriteButton)

        repository = AcademicRepository(applicationContext)

        tutorId = intent.getStringExtra("TUTOR_ID")
        val tutor = MockData.tutors.find { it.id == tutorId }

        if (tutor != null) {
            profileName.text = tutor.name
            profileType.text = if (tutor.type == TutorType.UNIVERSITY)
                "University Tutor" else "Peer Tutor"
            profileSubjects.text = tutor.subjects.joinToString(", ")
            profileCourses.text = tutor.courses.joinToString(", ")
            profileAvailability.text = tutor.availability
            profileRate.text = if (tutor.isPaid && tutor.rate != null)
                tutor.rate else "Free"

            requestSessionButton.setOnClickListener {
                val intent = Intent(this, SessionRequestActivity::class.java)
                intent.putExtra("TUTOR_ID", tutor.id)
                startActivity(intent)
            }

            rateButton.setOnClickListener {
                showRatingDialog(tutor.id)
            }

            favoriteButton.setOnClickListener {
                toggleFavorite(tutor.id)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        tutorId?.let {
            refreshRatings(it)
            refreshFavoriteStatus(it)
        }
    }

    private fun refreshFavoriteStatus(tutorId: String) {
        val currentUser = MockData.currentUser ?: return
        executor.execute {
            val favorited = repository.isFavorite(tutorId, currentUser.id)
            mainHandler.post {
                isFavorited = favorited
                updateFavoriteIcon()
            }
        }
    }

    private fun updateFavoriteIcon() {
        if (isFavorited) {
            favoriteButton.text = "♥"
            favoriteButton.setTextColor(ContextCompat.getColor(this, R.color.red_error))
        } else {
            favoriteButton.text = "♡"
            favoriteButton.setTextColor(Color.WHITE)
        }
    }

    private fun toggleFavorite(tutorId: String) {
        val currentUser = MockData.currentUser ?: return
        val wasFavorited = isFavorited
        executor.execute {
            if (wasFavorited) {
                repository.removeFavorite(tutorId, currentUser.id)
            } else {
                repository.addFavorite(tutorId, currentUser.id)
            }
            mainHandler.post {
                isFavorited = !wasFavorited
                updateFavoriteIcon()
                Toast.makeText(
                    this,
                    if (isFavorited) "Added to favorites" else "Removed from favorites",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun refreshRatings(tutorId: String) {
        val currentUser = MockData.currentUser
        executor.execute {
            val ratings = repository.getRatingsForTutor(tutorId)
            val average = repository.getAverageRatingForTutor(tutorId)
            val alreadyRated = if (currentUser != null) {
                repository.hasStudentRatedTutor(tutorId, currentUser.id)
            } else {
                true
            }

            mainHandler.post {
                val summary = String.format(
                    Locale.getDefault(),
                    "%.1f (%d review%s)",
                    average,
                    ratings.size,
                    if (ratings.size == 1) "" else "s"
                )
                profileRatingSummary.text = summary

                val canRate = currentUser != null &&
                    currentUser.role == UserRole.STUDENT &&
                    !alreadyRated
                rateButton.visibility = if (canRate) LinearLayout.VISIBLE else LinearLayout.GONE
            }
        }
    }

    private fun showRatingDialog(tutorId: String) {
        val currentUser = MockData.currentUser ?: return

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Rate this Tutor")
        val options = arrayOf("1 ★", "2 ★★", "3 ★★★", "4 ★★★★", "5 ★★★★★")
        builder.setItems(options) { dialog, which ->
            val stars = (which + 1).toFloat()
            submitRating(tutorId, currentUser.id, stars)
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { d, _ -> d.dismiss() }
        builder.show()
    }

    private fun submitRating(tutorId: String, studentId: String, rating: Float) {
        executor.execute {
            repository.insertRating(
                RatingEntity(
                    id = UUID.randomUUID().toString(),
                    tutorId = tutorId,
                    studentId = studentId,
                    rating = rating,
                    comment = ""
                )
            )
            mainHandler.post {
                Toast.makeText(this, "Thanks for rating!", Toast.LENGTH_SHORT).show()
                refreshRatings(tutorId)
            }
        }
    }
}
