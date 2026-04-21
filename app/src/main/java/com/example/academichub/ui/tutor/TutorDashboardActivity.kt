package com.example.academichub.ui.tutor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.AcademicHubApplication
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.model.RequestStatus
import com.example.academichub.model.SessionRequest

class TutorDashboardActivity : AppCompatActivity() {

    private lateinit var requestsRecyclerView: RecyclerView
    private lateinit var tutorNameText: TextView
    private lateinit var emptyStateText: TextView
    private lateinit var requestAdapter: RequestAdapter
    private lateinit var logoutButton: android.widget.Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor_dashboard)

        // Connect to UI elements
        requestsRecyclerView = findViewById(R.id.requestsRecyclerView)
        tutorNameText = findViewById(R.id.tutorNameText)
        emptyStateText = findViewById(R.id.emptyStateText)
        logoutButton = findViewById(R.id.logoutButton)

        logoutButton.setOnClickListener {
            val intent = Intent(this, com.example.academichub.ui.LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        // Show logged in tutor's name
        val currentUser = MockData.currentUser
        if (currentUser != null) {
            tutorNameText.text = currentUser.name
        }

        // Setup the requests list
        setupRequestsList()
    }

    private fun setupRequestsList() {
        // Load from Room Database instead of MockData
        val repository = (application as AcademicHubApplication).repository
        val dbRequests = repository.getAllSessionRequests()

        // Also sync with MockData for current session
        MockData.sessionRequests.clear()
        MockData.sessionRequests.addAll(dbRequests)

        val requests = MockData.sessionRequests

        // Show empty state if no requests
        if (requests.isEmpty()) {
            emptyStateText.visibility = View.VISIBLE
            requestsRecyclerView.visibility = View.GONE
            return
        }

        emptyStateText.visibility = View.GONE
        requestsRecyclerView.visibility = View.VISIBLE

        // Create adapter
        requestAdapter = RequestAdapter(
            requests,
            onAccept = { request -> handleAccept(request) },
            onReject = { request -> handleReject(request) }
        )

        // Setup RecyclerView
        requestsRecyclerView.layoutManager = LinearLayoutManager(this)
        requestsRecyclerView.adapter = requestAdapter
    }

    private fun handleAccept(request: SessionRequest) {
        val index = MockData.sessionRequests.indexOfFirst { it.id == request.id }
        if (index != -1) {
            MockData.sessionRequests[index] = request.copy(
                status = RequestStatus.ACCEPTED
            )
            requestAdapter.notifyItemChanged(index)

            // Save to Room Database
            val repository = (application as AcademicHubApplication).repository
            repository.updateRequestStatus(request.id, RequestStatus.ACCEPTED)

            android.widget.Toast.makeText(
                this,
                "Request from ${request.studentName} accepted!",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleReject(request: SessionRequest) {
        val index = MockData.sessionRequests.indexOfFirst { it.id == request.id }
        if (index != -1) {
            MockData.sessionRequests[index] = request.copy(
                status = RequestStatus.REJECTED
            )
            requestAdapter.notifyItemChanged(index)

            // Save to Room Database
            val repository = (application as AcademicHubApplication).repository
            repository.updateRequestStatus(request.id, RequestStatus.REJECTED)

            android.widget.Toast.makeText(
                this,
                "Request from ${request.studentName} rejected.",
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }
    }
}