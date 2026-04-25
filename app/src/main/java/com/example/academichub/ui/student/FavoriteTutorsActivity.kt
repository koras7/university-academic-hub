package com.example.academichub.ui.student

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.data.MockData
import com.example.academichub.database.AcademicRepository
import com.example.academichub.model.TutorProfile
import java.util.concurrent.Executors

class FavoriteTutorsActivity : AppCompatActivity() {

    private lateinit var favoriteTutorsRecyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var tutorAdapter: TutorAdapter
    private lateinit var repository: AcademicRepository

    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_tutors)

        favoriteTutorsRecyclerView = findViewById(R.id.favoriteTutorsRecyclerView)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        loadingIndicator = findViewById(R.id.loadingIndicator)

        repository = AcademicRepository(applicationContext)

        tutorAdapter = TutorAdapter(mutableListOf()) { tutor ->
            onTutorClicked(tutor)
        }
        favoriteTutorsRecyclerView.layoutManager = LinearLayoutManager(this)
        favoriteTutorsRecyclerView.adapter = tutorAdapter
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun loadFavorites() {
        val currentUser = MockData.currentUser ?: return
        loadingIndicator.visibility = View.VISIBLE
        executor.execute {
            val favorites = repository.getFavoriteTutors(currentUser.id)
            mainHandler.post {
                loadingIndicator.visibility = View.GONE
                tutorAdapter.updateTutors(favorites)
                if (favorites.isEmpty()) {
                    emptyStateLayout.visibility = View.VISIBLE
                    favoriteTutorsRecyclerView.visibility = View.GONE
                } else {
                    emptyStateLayout.visibility = View.GONE
                    favoriteTutorsRecyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun onTutorClicked(tutor: TutorProfile) {
        val intent = Intent(this, TutorProfileActivity::class.java)
        intent.putExtra("TUTOR_ID", tutor.id)
        startActivity(intent)
    }
}
