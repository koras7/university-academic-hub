package com.example.academichub.ui.student

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.model.TutorType

class TutorProfileActivity : AppCompatActivity() {

    private lateinit var profileName: TextView
    private lateinit var profileType: TextView
    private lateinit var profileSubjects: TextView
    private lateinit var profileCourses: TextView
    private lateinit var profileAvailability: TextView
    private lateinit var profileRate: TextView
    private lateinit var requestSessionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor_profile)

        // Connect to UI elements
        profileName = findViewById(R.id.profileName)
        profileType = findViewById(R.id.profileType)
        profileSubjects = findViewById(R.id.profileSubjects)
        profileCourses = findViewById(R.id.profileCourses)
        profileAvailability = findViewById(R.id.profileAvailability)
        profileRate = findViewById(R.id.profileRate)
        requestSessionButton = findViewById(R.id.requestSessionButton)

        // Get the tutor ID passed from TutorListActivity
        val tutorId = intent.getStringExtra("TUTOR_ID")

        // Find the tutor in our mock data
        val tutor = MockData.tutors.find { it.id == tutorId }

        if (tutor != null) {
            // Fill in the profile details
            profileName.text = tutor.name
            profileType.text = if (tutor.type == TutorType.UNIVERSITY)
                "University Tutor" else "Peer Tutor"
            profileSubjects.text = tutor.subjects.joinToString(", ")
            profileCourses.text = tutor.courses.joinToString(", ")
            profileAvailability.text = tutor.availability
            profileRate.text = if (tutor.isPaid && tutor.rate != null)
                tutor.rate else "Free"

            // Request session button
            requestSessionButton.setOnClickListener {
                android.widget.Toast.makeText(
                    this,
                    "Session request feature coming soon!",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}