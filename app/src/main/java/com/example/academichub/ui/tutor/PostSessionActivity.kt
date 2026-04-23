package com.example.academichub.ui.tutor

import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.model.TutoringSession
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.UUID

class PostSessionActivity : AppCompatActivity() {

    private lateinit var sessionSubjectInput: TextInputEditText
    private lateinit var sessionDescriptionInput: TextInputEditText
    private lateinit var sessionTimeInput: TextInputEditText
    private lateinit var sessionMaxStudentsInput: TextInputEditText
    private lateinit var isPaidSwitch: Switch
    private lateinit var rateInputLayout: TextInputLayout
    private lateinit var sessionRateInput: TextInputEditText
    private lateinit var postSessionButton: TextView
    private lateinit var postErrorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_session)

        // Connect to UI elements
        sessionSubjectInput = findViewById(R.id.sessionSubjectInput)
        sessionDescriptionInput = findViewById(R.id.sessionDescriptionInput)
        sessionTimeInput = findViewById(R.id.sessionTimeInput)
        sessionMaxStudentsInput = findViewById(R.id.sessionMaxStudentsInput)
        isPaidSwitch = findViewById(R.id.isPaidSwitch)
        rateInputLayout = findViewById(R.id.rateInputLayout)
        sessionRateInput = findViewById(R.id.sessionRateInput)
        postSessionButton = findViewById(R.id.postSessionButton)
        postErrorText = findViewById(R.id.postErrorText)

        // Show/hide rate input based on paid switch
        isPaidSwitch.setOnCheckedChangeListener { _, isChecked ->
            rateInputLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        // Post button click
        postSessionButton.setOnClickListener {
            handlePostSession()
        }
    }

    private fun handlePostSession() {
        val subject = sessionSubjectInput.text.toString().trim()
        val description = sessionDescriptionInput.text.toString().trim()
        val time = sessionTimeInput.text.toString().trim()
        val maxStudentsStr = sessionMaxStudentsInput.text.toString().trim()
        val isPaid = isPaidSwitch.isChecked
        val rate = sessionRateInput.text.toString().trim()

        // Validate inputs
        if (subject.isEmpty()) {
            showError("Please enter a subject")
            return
        }
        if (description.isEmpty()) {
            showError("Please enter a description")
            return
        }
        if (time.isEmpty()) {
            showError("Please enter available time")
            return
        }
        if (maxStudentsStr.isEmpty()) {
            showError("Please enter max students")
            return
        }
        if (isPaid && rate.isEmpty()) {
            showError("Please enter a rate for paid session")
            return
        }

        val currentUser = MockData.currentUser
        if (currentUser == null) {
            showError("Something went wrong. Please try again.")
            return
        }

        // Create tutoring session
        val session = TutoringSession(
            id = UUID.randomUUID().toString(),
            tutorId = currentUser.id,
            tutorName = currentUser.name,
            subject = subject,
            description = description,
            availableTime = time,
            maxStudents = maxStudentsStr.toInt(),
            isPaid = isPaid,
            rate = if (isPaid) rate else null
        )

        // Save to MockData
        MockData.tutoringSessions.add(session)

        // Show success
        android.widget.Toast.makeText(
            this,
            "Session posted successfully!",
            android.widget.Toast.LENGTH_LONG
        ).show()

        // Go back
        finish()
    }

    private fun showError(message: String) {
        postErrorText.text = message
        postErrorText.visibility = View.VISIBLE
    }
}