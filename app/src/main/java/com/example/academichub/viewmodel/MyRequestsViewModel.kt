package com.example.academichub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.academichub.AcademicHubApplication
import com.example.academichub.model.SessionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyRequestsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as AcademicHubApplication).repository

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _requests = MutableLiveData<List<SessionRequest>>(emptyList())
    val requests: LiveData<List<SessionRequest>> = _requests

    fun loadRequests() {
        _isLoading.value = true
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                repository.getAllSessionRequests()
            }
            _requests.value = data
            _isLoading.value = false
        }
    }
}
