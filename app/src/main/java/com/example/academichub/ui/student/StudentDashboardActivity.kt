package com.example.academichub.ui.student

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.academichub.R
import com.example.academichub.data.MockData


class StudentDashboardActivity : AppCompatActivity() {

    private lateinit var welcomeText: TextView
    private lateinit var userNameText: TextView
    private lateinit var findTutorsCard: CardView
    private lateinit var myRequestsCard: CardView

    private lateinit var logoutButton: android.widget.Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dashboard)

        // Connect to UI elements
        welcomeText = findViewById(R.id.welcomeText)
        userNameText = findViewById(R.id.userNameText)
        findTutorsCard = findViewById(R.id.findTutorsCard)
        myRequestsCard = findViewById(R.id.myRequestsCard)

        logoutButton = findViewById(R.id.logoutButton)

        logoutButton.setOnClickListener {
            val intent = Intent(this, com.example.academichub.ui.LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        // Show the logged in user's name
        val currentUser = MockData.currentUser
        if (currentUser != null) {
            welcomeText.text = "Welcome, ${currentUser.name}!"
            userNameText.text = currentUser.name
        }

        // Find Tutors card click
        findTutorsCard.setOnClickListener {
            val intent = Intent(this, TutorListActivity::class.java)
            startActivity(intent)
        }

        // My Requests card click
        myRequestsCard.setOnClickListener {
            android.widget.Toast.makeText(
                this,
                "Requests coming soon!",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }
}