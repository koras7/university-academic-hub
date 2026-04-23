package com.example.academichub.ui.student

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.viewmodel.SessionRequestViewModel
import com.google.android.material.textfield.TextInputEditText

class SessionRequestActivity : AppCompatActivity() {

    private lateinit var tutorNameText: TextView
    private lateinit var subjectInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText
    private lateinit var noteInput: TextInputEditText
    private lateinit var submitRequestButton: TextView
    private lateinit var requestErrorText: TextView
    private lateinit var viewModel: SessionRequestViewModel

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

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[SessionRequestViewModel::class.java]

        // Get tutor ID passed from TutorProfileActivity
        val tutorId = intent.getStringExtra("TUTOR_ID")
        val tutor = MockData.tutors.find { it.id == tutorId }

        // Show tutor name on the form
        if (tutor != null) {
            tutorNameText.text = tutor.name
        }

        observeViewModel()

        // Submit button click
        submitRequestButton.setOnClickListener {
            handleSubmit(tutorId, tutor?.name)
        }
    }

    private fun observeViewModel() {
        viewModel.submitResult.observe(this) { result ->
            when (result) {
                is SessionRequestViewModel.SubmitResult.Success -> {
                    MockData.sessionRequests.add(result.request)
                    requestErrorText.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Request sent to ${result.request.tutorName}!",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
                is SessionRequestViewModel.SubmitResult.Error -> {
                    showError(result.message)
                }
            }
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

        viewModel.submitRequest(
            studentId = currentUser.id,
            studentName = currentUser.name,
            tutorId = tutorId,
            tutorName = tutorName,
            subject = subject,
            preferredTime = time,
            note = note
        )
    }

    private fun showError(message: String) {
        requestErrorText.text = message
        requestErrorText.visibility = View.VISIBLE
    }
}
