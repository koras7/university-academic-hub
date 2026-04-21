package com.example.academichub.ui.student

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.AcademicHubApplication
import com.example.academichub.R
import com.example.academichub.data.MockData

class MyRequestsActivity : AppCompatActivity() {

    private lateinit var myRequestsRecyclerView: RecyclerView
    private lateinit var emptyStateText: TextView
    private lateinit var myRequestsAdapter: MyRequestsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_requests)

        myRequestsRecyclerView = findViewById(R.id.myRequestsRecyclerView)
        emptyStateText = findViewById(R.id.emptyStateText)

        setupRequestsList()
    }

    private fun setupRequestsList() {
        val currentUser = MockData.currentUser

        // Load from Room Database
        val repository = (application as AcademicHubApplication).repository
        val allRequests = repository.getAllSessionRequests()

        val myRequests = allRequests

        if (myRequests.isEmpty()) {
            emptyStateText.visibility = View.VISIBLE
            myRequestsRecyclerView.visibility = View.GONE
            return
        }

        emptyStateText.visibility = View.GONE
        myRequestsRecyclerView.visibility = View.VISIBLE

        myRequestsAdapter = MyRequestsAdapter(myRequests)
        myRequestsRecyclerView.layoutManager = LinearLayoutManager(this)
        myRequestsRecyclerView.adapter = myRequestsAdapter
    }
}