package com.example.academichub.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.model.User
import com.example.academichub.model.UserRole
import com.google.android.material.textfield.TextInputEditText
import java.util.UUID
import com.example.academichub.ui.student.StudentDashboardActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: TextInputEditText
    private lateinit var roleRadioGroup: RadioGroup
    private lateinit var loginButton: Button
    private lateinit var errorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Connect our variables to the UI elements
        emailInput = findViewById(R.id.emailInput)
        roleRadioGroup = findViewById(R.id.roleRadioGroup)
        loginButton = findViewById(R.id.loginButton)
        errorText = findViewById(R.id.errorText)

        // What happens when login button is clicked
        loginButton.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin() {
        val email = emailInput.text.toString().trim()

        // Validate email
        if (email.isEmpty()) {
            showError("Please enter your university email")
            return
        }

        if (!email.endsWith(".edu")) {
            showError("Please use a valid university email (.edu)")
            return
        }

        // Get selected role
        val selectedRole = when (roleRadioGroup.checkedRadioButtonId) {
            R.id.studentRadio -> UserRole.STUDENT
            R.id.tutorRadio -> UserRole.PEER_TUTOR
            R.id.professorRadio -> UserRole.PROFESSOR
            else -> UserRole.STUDENT
        }

        // Create the logged-in user and save to MockData
        MockData.currentUser = User(
            id = UUID.randomUUID().toString(),
            name = email.substringBefore("@"),
            email = email,
            role = selectedRole
        )

        // Hide error if visible
        errorText.visibility = View.GONE

        // Navigate based on role (we'll create these activities next)
        navigateToDashboard(selectedRole)
    }

    private fun showError(message: String) {
        errorText.text = message
        errorText.visibility = View.VISIBLE
    }

    private fun navigateToDashboard(role: UserRole) {
        val intent = when (role) {
            UserRole.STUDENT -> Intent(this, StudentDashboardActivity::class.java)
            UserRole.PEER_TUTOR -> Intent(this, StudentDashboardActivity::class.java)
            UserRole.UNIVERSITY_TUTOR -> Intent(this, StudentDashboardActivity::class.java)
            UserRole.PROFESSOR -> Intent(this, StudentDashboardActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}