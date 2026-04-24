package com.example.academichub.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.academichub.AcademicHubApplication
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.model.User
import com.example.academichub.model.UserRole
import com.example.academichub.ui.professor.ProfessorDashboardActivity
import com.example.academichub.ui.student.StudentDashboardActivity
import com.example.academichub.ui.tutor.TutorDashboardActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var errorText: TextView
    private lateinit var signUpText: TextView

    companion object {
        const val PREFS_NAME = "academic_hub_prefs"
        const val KEY_SESSION_USER_ID = "session_user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // If there's an active session, skip login
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val sessionUserId = prefs.getString(KEY_SESSION_USER_ID, null)
        if (!sessionUserId.isNullOrEmpty()) {
            val repository = (application as AcademicHubApplication).repository
            val user = repository.getUserById(sessionUserId)
            if (user != null) {
                MockData.currentUser = User(
                    id = user.id,
                    name = user.name,
                    email = user.email,
                    role = UserRole.valueOf(user.role)
                )
                navigateToDashboard(UserRole.valueOf(user.role))
                return
            } else {
                // Stale session — clear it.
                prefs.edit().remove(KEY_SESSION_USER_ID).apply()
            }
        }

        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        errorText = findViewById(R.id.errorText)
        signUpText = findViewById(R.id.signUpText)

        loginButton.setOnClickListener { handleLogin() }

        signUpText.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun handleLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()

        if (email.isEmpty()) {
            showError("Please enter your university email")
            return
        }
        if (!email.endsWith(".edu")) {
            showError("Please use a valid university email (.edu)")
            return
        }
        if (password.isEmpty()) {
            showError("Please enter your password")
            return
        }

        val repository = (application as AcademicHubApplication).repository
        val user = repository.loginUser(email, password)
        if (user == null) {
            showError("Invalid email or password")
            return
        }

        errorText.visibility = View.GONE

        val role = UserRole.valueOf(user.role)
        MockData.currentUser = User(
            id = user.id,
            name = user.name,
            email = user.email,
            role = role
        )

        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_SESSION_USER_ID, user.id)
            .apply()

        navigateToDashboard(role)
    }

    private fun showError(message: String) {
        errorText.text = message
        errorText.visibility = View.VISIBLE
    }

    private fun navigateToDashboard(role: UserRole) {
        val intent = when (role) {
            UserRole.STUDENT -> Intent(this, StudentDashboardActivity::class.java)
            UserRole.PEER_TUTOR -> Intent(this, TutorDashboardActivity::class.java)
            UserRole.UNIVERSITY_TUTOR -> Intent(this, TutorDashboardActivity::class.java)
            UserRole.PROFESSOR -> Intent(this, ProfessorDashboardActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
