package com.example.academichub.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.academichub.AcademicHubApplication
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.model.User
import com.example.academichub.model.UserRole
import com.example.academichub.ui.student.StudentDashboardActivity
import com.example.academichub.ui.tutor.TutorDashboardActivity
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {

    private lateinit var nameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var roleRadioGroup: RadioGroup
    private lateinit var signUpButton: Button
    private lateinit var errorText: TextView
    private lateinit var backToLoginText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput)
        roleRadioGroup = findViewById(R.id.roleRadioGroup)
        signUpButton = findViewById(R.id.signUpButton)
        errorText = findViewById(R.id.errorText)
        backToLoginText = findViewById(R.id.backToLoginText)

        signUpButton.setOnClickListener { handleSignUp() }
        backToLoginText.setOnClickListener { finish() }
    }

    private fun handleSignUp() {
        val name = nameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString()
        val confirmPassword = confirmPasswordInput.text.toString()

        if (name.isEmpty()) {
            showError("Please enter your full name")
            return
        }
        if (email.isEmpty()) {
            showError("Please enter your university email")
            return
        }
        if (!email.endsWith(".edu")) {
            showError("Please use a valid university email (.edu)")
            return
        }
        if (password.length < 6) {
            showError("Password must be at least 6 characters")
            return
        }
        if (password != confirmPassword) {
            showError("Passwords do not match")
            return
        }

        val role = getSelectedRole()
        val repository = (application as AcademicHubApplication).repository
        val created = repository.registerUser(
            name = name,
            email = email,
            password = password,
            role = role.name
        )
        if (!created) {
            showError("An account with this email already exists")
            return
        }

        errorText.visibility = View.GONE

        // Auto-login the newly created user
        val user = repository.loginUser(email, password) ?: run {
            showError("Something went wrong. Please log in.")
            return
        }

        MockData.currentUser = User(
            id = user.id,
            name = user.name,
            email = user.email,
            role = UserRole.valueOf(user.role)
        )

        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_SESSION_USER_ID, user.id)
            .apply()

        navigateToDashboard(UserRole.valueOf(user.role))
    }

    private fun getSelectedRole(): UserRole = when (roleRadioGroup.checkedRadioButtonId) {
        R.id.studentRadio -> UserRole.STUDENT
        R.id.tutorRadio -> UserRole.PEER_TUTOR
        R.id.professorRadio -> UserRole.PROFESSOR
        else -> UserRole.STUDENT
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
            UserRole.PROFESSOR -> Intent(this, StudentDashboardActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    companion object {
        const val PREFS_NAME = "academic_hub_prefs"
        const val KEY_SESSION_USER_ID = "session_user_id"
    }
}
