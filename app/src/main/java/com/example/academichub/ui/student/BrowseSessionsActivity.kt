package com.example.academichub.ui.student

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.data.MockData

class BrowseSessionsActivity : AppCompatActivity() {

    private lateinit var sessionsRecyclerView: RecyclerView
    private lateinit var emptySessionsText: TextView
    private lateinit var sessionsCountText: TextView
    private lateinit var sessionsAdapter: SessionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse_sessions)

        sessionsRecyclerView = findViewById(R.id.sessionsRecyclerView)
        emptySessionsText = findViewById(R.id.emptySessionsText)
        sessionsCountText = findViewById(R.id.sessionsCountText)

        setupSessionsList()
    }

    private fun setupSessionsList() {
        val sessions = MockData.tutoringSessions

        if (sessions.isEmpty()) {
            emptySessionsText.visibility = View.VISIBLE
            sessionsRecyclerView.visibility = View.GONE
            sessionsCountText.visibility = View.GONE
            return
        }

        emptySessionsText.visibility = View.GONE
        sessionsRecyclerView.visibility = View.VISIBLE
        sessionsCountText.visibility = View.VISIBLE
        sessionsCountText.text = "${sessions.size} sessions available"

        sessionsAdapter = SessionsAdapter(sessions)
        sessionsRecyclerView.layoutManager = LinearLayoutManager(this)
        sessionsRecyclerView.adapter = sessionsAdapter
    }
}