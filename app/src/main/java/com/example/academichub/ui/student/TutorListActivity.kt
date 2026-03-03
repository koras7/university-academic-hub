package com.example.academichub.ui.student

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.model.TutorProfile
import android.content.Intent

class TutorListActivity : AppCompatActivity() {

    private lateinit var tutorRecyclerView: RecyclerView
    private lateinit var tutorCountText: TextView
    private lateinit var tutorAdapter: TutorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor_list)

        // Connect to UI elements
        tutorRecyclerView = findViewById(R.id.tutorRecyclerView)
        tutorCountText = findViewById(R.id.tutorCountText)

        // Setup the tutor list
        setupTutorList()
    }

    private fun setupTutorList() {
        val tutors = MockData.tutors

        // Show how many tutors found
        tutorCountText.text = "${tutors.size} tutors available"

        // Create the adapter
        tutorAdapter = TutorAdapter(tutors) { tutor ->
            onTutorClicked(tutor)
        }

        // Setup RecyclerView
        tutorRecyclerView.layoutManager = LinearLayoutManager(this)
        tutorRecyclerView.adapter = tutorAdapter
    }

    private fun onTutorClicked(tutor: TutorProfile) {
        val intent = Intent(this, TutorProfileActivity::class.java)
        intent.putExtra("TUTOR_ID", tutor.id)
        startActivity(intent)
    }
}