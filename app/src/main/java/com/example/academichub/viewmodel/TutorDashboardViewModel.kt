package com.example.academichub.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.academichub.AcademicHubApplication
import com.example.academichub.model.RequestStatus
import com.example.academichub.model.SessionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TutorDashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as AcademicHubApplication).repository

    private val _requests = MutableLiveData<List<SessionRequest>>(emptyList())
    val requests: LiveData<List<SessionRequest>> = _requests

    fun loadRequests() {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                repository.getAllSessionRequests()
            }
            _requests.value = data
        }
    }

    fun loadRequestsForTutor(tutorId: String) {
        viewModelScope.launch {
            val data = withContext(Dispatchers.IO) {
                repository.getAllSessionRequests().filter { it.tutorId == tutorId }
            }
            _requests.value = data
        }
    }

    fun acceptRequest(request: SessionRequest) {
        updateStatus(request, RequestStatus.ACCEPTED)
    }

    fun rejectRequest(request: SessionRequest) {
        updateStatus(request, RequestStatus.REJECTED)
    }

    private fun updateStatus(request: SessionRequest, status: RequestStatus) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateRequestStatus(request.id, status)
            }
            _requests.value = _requests.value.orEmpty().map {
                if (it.id == request.id) it.copy(status = status) else it
            }
        }
    }
}
