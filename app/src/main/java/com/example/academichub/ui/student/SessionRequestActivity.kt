package com.example.academichub.ui.student

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.model.RequestStatus
import com.example.academichub.model.SessionRequest
import com.google.android.material.textfield.TextInputEditText
import java.util.UUID

class SessionRequestActivity : AppCompatActivity() {

    private lateinit var tutorNameText: TextView
    private lateinit var subjectInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText
    private lateinit var noteInput: TextInputEditText
    private lateinit var submitRequestButton: Button
    private lateinit var requestErrorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session_request)

        // Connect to UI elements
        tutorNameText = findViewById(R.id.tutorNameText)
        subjectInput = findViewById(R.id.subjectInput)
        timeInput = findViewById(R.id.timeInput)
        noteInput = findViewById(R.id.noteInput)
        submitRequestButton = findViewById(R.id.submitRequestButton)
        requestErrorText = findViewById(R.id.requestErrorText)

        // Get tutor ID passed from TutorProfileActivity
        val tutorId = intent.getStringExtra("TUTOR_ID")
        val tutor = MockData.tutors.find { it.id == tutorId }

        // Show tutor name on the form
        if (tutor != null) {
            tutorNameText.text = tutor.name
        }

        // Submit button click
        submitRequestButton.setOnClickListener {
            handleSubmit(tutorId, tutor?.name)
        }
    }

    private fun handleSubmit(tutorId: String?, tutorName: String?) {
        val subject = subjectInput.text.toString().trim()
        val time = timeInput.text.toString().trim()
        val note = noteInput.text.toString().trim()

        // Validate inputs
        if (subject.isEmpty()) {
            showError("Please enter a subject")
            return
        }

        if (time.isEmpty()) {
            showError("Please enter a preferred time")
            return
        }

        // Get current logged in student
        val currentUser = MockData.currentUser

        if (currentUser == null || tutorId == null || tutorName == null) {
            showError("Something went wrong. Please try again.")
            return
        }

        // Create the session request
        val request = SessionRequest(
            id = UUID.randomUUID().toString(),
            studentId = currentUser.id,
            studentName = currentUser.name,
            tutorId = tutorId,
            tutorName = tutorName,
            subject = subject,
            preferredTime = time,
            note = note,
            status = RequestStatus.PENDING
        )

        // Save to Room Database
        val repository = (application as com.example.academichub.AcademicHubApplication).repository
        repository.insertSessionRequest(request)

        // Also keep in MockData for current session display
        MockData.sessionRequests.add(request)

        // Hide error if visible
        requestErrorText.visibility = View.GONE

        // Show success and go back
        android.widget.Toast.makeText(
            this,
            "Request sent to ${tutorName}!",
            android.widget.Toast.LENGTH_LONG
        ).show()

        // Go back to tutor profile
        finish()
    }

    private fun showError(message: String) {
        requestErrorText.text = message
        requestErrorText.visibility = View.VISIBLE
    }
}