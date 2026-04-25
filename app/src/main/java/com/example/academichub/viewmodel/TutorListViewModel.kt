package com.example.academichub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academichub.data.MockData
import com.example.academichub.model.TutorProfile

class TutorListViewModel : ViewModel() {

    private val allTutors: List<TutorProfile> = MockData.tutors

    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _tutors = MutableLiveData<List<TutorProfile>>(allTutors)
    val tutors: LiveData<List<TutorProfile>> = _tutors

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        _tutors.value = filter(query)
    }

    fun clearSearch() {
        setSearchQuery("")
    }

    private fun filter(query: String): List<TutorProfile> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return allTutors
        return allTutors.filter { tutor ->
            tutor.name.contains(trimmed, ignoreCase = true) ||
                tutor.subjects.any { it.contains(trimmed, ignoreCase = true) } ||
                tutor.courses.any { it.contains(trimmed, ignoreCase = true) }
        }
    }
}
