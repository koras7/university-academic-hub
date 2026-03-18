package com.example.academichub.ui.professor

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.data.MockData

class ProfessorDirectoryActivity : AppCompatActivity() {

    private lateinit var professorRecyclerView: RecyclerView
    private lateinit var professorCountText: TextView
    private lateinit var professorAdapter: ProfessorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professor_directory)

        professorRecyclerView = findViewById(R.id.professorRecyclerView)
        professorCountText = findViewById(R.id.professorCountText)

        setupProfessorList()
    }

    private fun setupProfessorList() {
        val professors = MockData.professors

        professorCountText.text = "${professors.size} professors available"

        professorAdapter = ProfessorAdapter(professors)

        professorRecyclerView.layoutManager = LinearLayoutManager(this)
        professorRecyclerView.adapter = professorAdapter
    }
}