package com.example.academichub.ui.student

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.model.TutorProfile
import com.example.academichub.viewmodel.TutorListViewModel

class TutorListActivity : AppCompatActivity() {

    private lateinit var tutorRecyclerView: RecyclerView
    private lateinit var tutorCountText: TextView
    private lateinit var searchInput: EditText
    private lateinit var tutorAdapter: TutorAdapter
    private lateinit var viewModel: TutorListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor_list)

        // Connect to UI elements
        tutorRecyclerView = findViewById(R.id.tutorRecyclerView)
        tutorCountText = findViewById(R.id.tutorCountText)
        searchInput = findViewById(R.id.searchInput)

        viewModel = ViewModelProvider(this)[TutorListViewModel::class.java]

        setupTutorList()
        observeViewModel()
        setupSearch()
    }

    private fun setupTutorList() {
        tutorAdapter = TutorAdapter(mutableListOf()) { tutor ->
            onTutorClicked(tutor)
        }

        tutorRecyclerView.layoutManager = LinearLayoutManager(this)
        tutorRecyclerView.adapter = tutorAdapter
    }

    private fun observeViewModel() {
        viewModel.tutors.observe(this) { tutors ->
            tutorCountText.text = "${tutors.size} tutors available"
            tutorAdapter.updateTutors(tutors)
        }
    }

    private fun setupSearch() {
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.setSearchQuery(s.toString())
            }
        })
    }

    private fun onTutorClicked(tutor: TutorProfile) {
        val intent = Intent(this, TutorProfileActivity::class.java)
        intent.putExtra("TUTOR_ID", tutor.id)
        startActivity(intent)
    }
}
