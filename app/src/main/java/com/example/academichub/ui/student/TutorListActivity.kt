package com.example.academichub.ui.student

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.model.TutorProfile
import com.google.android.material.textfield.TextInputEditText

class TutorListActivity : AppCompatActivity() {

    private lateinit var tutorRecyclerView: RecyclerView
    private lateinit var tutorCountText: TextView
    private lateinit var searchInput: TextInputEditText
    private lateinit var tutorAdapter: TutorAdapter

    // Keep original list for filtering
    private var allTutors: List<TutorProfile> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor_list)

        // Connect to UI elements
        tutorRecyclerView = findViewById(R.id.tutorRecyclerView)
        tutorCountText = findViewById(R.id.tutorCountText)
        searchInput = findViewById(R.id.searchInput)

        // Setup the tutor list
        setupTutorList()

        // Setup search
        setupSearch()
    }

    private fun setupTutorList() {
        allTutors = MockData.tutors

        // Show how many tutors found
        tutorCountText.text = "${allTutors.size} tutors available"

        // Create the adapter
        tutorAdapter = TutorAdapter(allTutors.toMutableList()) { tutor ->
            onTutorClicked(tutor)
        }

        // Setup RecyclerView
        tutorRecyclerView.layoutManager = LinearLayoutManager(this)
        tutorRecyclerView.adapter = tutorAdapter
    }

    private fun setupSearch() {
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterTutors(s.toString())
            }
        })
    }

    private fun filterTutors(query: String) {
        val filtered = if (query.isEmpty()) {
            allTutors
        } else {
            allTutors.filter { tutor ->
                tutor.subjects.any { it.contains(query, ignoreCase = true) } ||
                        tutor.courses.any { it.contains(query, ignoreCase = true) } ||
                        tutor.name.contains(query, ignoreCase = true)
            }
        }

        // Update count
        tutorCountText.text = "${filtered.size} tutors available"

        // Update adapter
        tutorAdapter.updateTutors(filtered)
    }

    private fun onTutorClicked(tutor: TutorProfile) {
        val intent = Intent(this, TutorProfileActivity::class.java)
        intent.putExtra("TUTOR_ID", tutor.id)
        startActivity(intent)
    }
}