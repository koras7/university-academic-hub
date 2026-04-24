package com.example.academichub.ui.student

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.ui.LoginActivity
import com.example.academichub.ui.professor.ProfessorDirectoryActivity
import java.util.Calendar


class StudentDashboardActivity : AppCompatActivity() {

    private lateinit var welcomeText: TextView
    private lateinit var userNameText: TextView
    private lateinit var findTutorsCard: CardView
    private lateinit var myRequestsCard: CardView

    private lateinit var browseSessionsCard: androidx.cardview.widget.CardView

    private lateinit var professorDirectoryCard: androidx.cardview.widget.CardView
    private lateinit var savedCard: androidx.cardview.widget.CardView
    private lateinit var logoutButton: android.widget.TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)

        // Connect to UI elements
        welcomeText = findViewById(R.id.welcomeText)
        userNameText = findViewById(R.id.userNameText)
        findTutorsCard = findViewById(R.id.findTutorsCard)
        myRequestsCard = findViewById(R.id.myRequestsCard)

        professorDirectoryCard = findViewById(R.id.professorDirectoryCard)

        professorDirectoryCard.setOnClickListener {
            val intent = Intent(this, ProfessorDirectoryActivity::class.java)
            startActivity(intent)
        }

        browseSessionsCard = findViewById(R.id.browseSessionsCard)

        browseSessionsCard.setOnClickListener {
            val intent = Intent(this, BrowseSessionsActivity::class.java)
            startActivity(intent)
        }

        savedCard = findViewById(R.id.savedCard)

        savedCard.setOnClickListener {
            val intent = Intent(this, FavoriteTutorsActivity::class.java)
            startActivity(intent)
        }

        logoutButton = findViewById(R.id.logoutButton)

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

        // Time-based greeting + logged-in user's name
        welcomeText.text = greetingForCurrentHour()
        val currentUser = MockData.currentUser
        if (currentUser != null) {
            userNameText.text = currentUser.name
            val avatarInitialText = findViewById<android.widget.TextView>(R.id.avatarInitialText)
            avatarInitialText.text = currentUser.name.first().uppercaseChar().toString()
        }

        // Find Tutors card click
        findTutorsCard.setOnClickListener {
            val intent = Intent(this, TutorListActivity::class.java)
            startActivity(intent)
        }

        // My Requests card click

        myRequestsCard.setOnClickListener {
            val intent = Intent(this, MyRequestsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun greetingForCurrentHour(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "GOOD MORNING"
            hour < 18 -> "GOOD AFTERNOON"
            else -> "GOOD EVENING"
        }
    }
}