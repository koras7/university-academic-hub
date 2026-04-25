package com.example.academichub.ui.student

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.academichub.R
import com.example.academichub.viewmodel.MyRequestsViewModel

class MyRequestsActivity : AppCompatActivity() {

    private lateinit var myRequestsRecyclerView: RecyclerView
    private lateinit var emptyStateText: TextView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var myRequestsAdapter: MyRequestsAdapter
    private lateinit var viewModel: MyRequestsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_requests)

        myRequestsRecyclerView = findViewById(R.id.myRequestsRecyclerView)
        emptyStateText = findViewById(R.id.emptyStateText)
        loadingIndicator = findViewById(R.id.loadingIndicator)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[MyRequestsViewModel::class.java]

        setupRequestsList()
        observeViewModel()

        viewModel.loadRequests()
    }

    private fun setupRequestsList() {
        myRequestsAdapter = MyRequestsAdapter(mutableListOf())
        myRequestsRecyclerView.layoutManager = LinearLayoutManager(this)
        myRequestsRecyclerView.adapter = myRequestsAdapter
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { loading ->
            loadingIndicator.visibility = if (loading) View.VISIBLE else View.GONE
        }
        viewModel.requests.observe(this) { requests ->
            loadingIndicator.visibility = View.GONE
            if (requests.isEmpty()) {
                emptyStateText.visibility = View.VISIBLE
                myRequestsRecyclerView.visibility = View.GONE
            } else {
                emptyStateText.visibility = View.GONE
                myRequestsRecyclerView.visibility = View.VISIBLE
                myRequestsAdapter.updateRequests(requests)
            }
        }
    }
}
