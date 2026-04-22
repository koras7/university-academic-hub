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
import java.util.UUID

class SessionRequestViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as AcademicHubApplication).repository

    private val _submitResult = MutableLiveData<SubmitResult>()
    val submitResult: LiveData<SubmitResult> = _submitResult

    fun submitRequest(
        studentId: String,
        studentName: String,
        tutorId: String,
        tutorName: String,
        subject: String,
        preferredTime: String,
        note: String
    ) {
        if (subject.isBlank() || preferredTime.isBlank()) {
            _submitResult.value = SubmitResult.Error("Subject and preferred time are required")
            return
        }

        val request = SessionRequest(
            id = UUID.randomUUID().toString(),
            studentId = studentId,
            studentName = studentName,
            tutorId = tutorId,
            tutorName = tutorName,
            subject = subject,
            preferredTime = preferredTime,
            note = note,
            status = RequestStatus.PENDING
        )

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertSessionRequest(request)
            }
            _submitResult.value = SubmitResult.Success(request)
        }
    }

    sealed class SubmitResult {
        data class Success(val request: SessionRequest) : SubmitResult()
        data class Error(val message: String) : SubmitResult()
    }
}
