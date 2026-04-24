package com.example.academichub.ui.tutor

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.model.SessionRequest
import com.example.academichub.ui.LoginActivity
import com.example.academichub.util.NotificationHelper
import com.example.academichub.viewmodel.TutorDashboardViewModel

class TutorDashboardActivity : AppCompatActivity() {

    private lateinit var requestsRecyclerView: RecyclerView
    private lateinit var tutorNameText: TextView
    private lateinit var emptyStateText: TextView
    private lateinit var requestAdapter: RequestAdapter
    private lateinit var logoutButton: android.widget.TextView

    private lateinit var postSessionButton: android.widget.TextView

    private lateinit var viewModel: TutorDashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor_dashboard)

        // Connect to UI elements
        requestsRecyclerView = findViewById(R.id.requestsRecyclerView)
        tutorNameText = findViewById(R.id.tutorNameText)
        emptyStateText = findViewById(R.id.emptyStateText)
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

        postSessionButton = findViewById(R.id.postSessionButton)

        postSessionButton.setOnClickListener {
            val intent = Intent(this, PostSessionActivity::class.java)
            startActivity(intent) }

        // Show logged in tutor's name
        val currentUser = MockData.currentUser
        if (currentUser != null) {
            tutorNameText.text = currentUser.name
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[TutorDashboardViewModel::class.java]

        setupRequestsList()
        observeViewModel()

        viewModel.loadRequests()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    private fun setupRequestsList() {
        requestAdapter = RequestAdapter(
            mutableListOf(),
            onAccept = { request -> handleAccept(request) },
            onReject = { request -> handleReject(request) }
        )

        requestsRecyclerView.layoutManager = LinearLayoutManager(this)
        requestsRecyclerView.adapter = requestAdapter
    }

    private fun observeViewModel() {
        viewModel.requests.observe(this) { requests ->
            if (requests.isEmpty()) {
                emptyStateText.visibility = View.VISIBLE
                requestsRecyclerView.visibility = View.GONE
            } else {
                emptyStateText.visibility = View.GONE
                requestsRecyclerView.visibility = View.VISIBLE
            }
            requestAdapter.updateRequests(requests)
        }
    }

    private fun handleAccept(request: SessionRequest) {
        viewModel.acceptRequest(request)
        NotificationHelper.showRequestAcceptedNotification(this, request.tutorName, request.subject)
        android.widget.Toast.makeText(
            this,
            "Request from ${request.studentName} accepted!",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }

    private fun handleReject(request: SessionRequest) {
        viewModel.rejectRequest(request)
        NotificationHelper.showRequestRejectedNotification(this, request.tutorName, request.subject)
        android.widget.Toast.makeText(
            this,
            "Request from ${request.studentName} rejected.",
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}
