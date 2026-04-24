package com.example.academichub.ui.professor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.academichub.AcademicHubApplication
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.ui.LoginActivity

class ProfessorDashboardActivity : AppCompatActivity() {

    private lateinit var professorNameText: TextView
    private lateinit var officeHoursText: TextView
    private lateinit var locationText: TextView
    private lateinit var coursesText: TextView
    private lateinit var requestCountText: TextView
    private lateinit var logoutButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor_dashboard)

        professorNameText = findViewById(R.id.professorNameText)
        officeHoursText = findViewById(R.id.officeHoursText)
        locationText = findViewById(R.id.locationText)
        coursesText = findViewById(R.id.coursesText)
        requestCountText = findViewById(R.id.requestCountText)
        logoutButton = findViewById(R.id.logoutButton)

        val currentUser = MockData.currentUser
        if (currentUser != null) {
            professorNameText.text = currentUser.name

            val professorInfo = MockData.professors.find { it.email == currentUser.email }
                ?: MockData.professors.firstOrNull()
            if (professorInfo != null) {
                officeHoursText.text = professorInfo.officeHours
                locationText.text = professorInfo.location
                coursesText.text = professorInfo.courses.joinToString("  •  ")
            } else {
                officeHoursText.text = "Not configured"
                locationText.text = "Not configured"
                coursesText.text = "Not configured"
            }
        }

        val repository = (application as AcademicHubApplication).repository
        requestCountText.text = repository.getAllSessionRequests().size.toString()

        logoutButton.setOnClickListener {
            getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(LoginActivity.KEY_SESSION_USER_ID)
                .apply()
            MockData.currentUser = null
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}
