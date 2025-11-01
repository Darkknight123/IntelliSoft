package com.app.intellisoft.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.intellisoft.data.remote.models.AddVisitsRequestModel
import com.app.intellisoft.data.remote.models.AddVitalsRequestModel
import com.app.intellisoft.data.remote.models.AddVitalsResponseData
import com.app.intellisoft.data.remote.models.Patient
import com.app.intellisoft.data.remote.models.PatientRequest
import com.app.intellisoft.data.repository.PatientRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PatientUiState {
    object Idle : PatientUiState()
    object Loading : PatientUiState()
    data class Success(val patients: List<Patient>) : PatientUiState()
    data class VitalsSuccess(val vitals: AddVitalsResponseData) : PatientUiState()
    data class Error(val message: String) : PatientUiState()
}

class PatientViewModel(
    private val repository: PatientRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PatientUiState>(PatientUiState.Idle)
    val uiState: StateFlow<PatientUiState> = _uiState

    fun registerPatient(request: PatientRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = PatientUiState.Loading
            try {
                val response = repository.registerPatient(request)
                if (response?.isSuccessful == true) {
                    onSuccess()
                } else {
                    _uiState.value = PatientUiState.Error(response?.message() ?: "Registration failed")
                }
            } catch (e: Exception) {
                _uiState.value = PatientUiState.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }

    fun getPatients() {
        viewModelScope.launch {
            _uiState.value = PatientUiState.Loading
            try {
                val response = repository.getPatients()
                if (response?.isSuccessful == true) {
                    _uiState.value = PatientUiState.Success(response.body()?.data ?: emptyList())
                } else {
                    Log.e("TAG", "getPatients: " + Gson().toJson(response) )
                    _uiState.value = PatientUiState.Error(response?.message() ?: "Failed to load patients")
                }
            } catch (e: Exception) {
                _uiState.value = PatientUiState.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }

    fun addVitals(request: AddVitalsRequestModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = PatientUiState.Loading
            try {
                val response = repository.addVitals(request)
                if (response?.isSuccessful == true) {
                    onSuccess()
                } else {
                    _uiState.value = PatientUiState.Error(response?.message() ?: "adding vitals failed")
                }
            } catch (e: Exception) {
                _uiState.value = PatientUiState.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }

    fun addVisit(request: AddVisitsRequestModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = PatientUiState.Loading
            try {
                val response = repository.addVisit(request)
                if (response?.isSuccessful == true) {
                    onSuccess()
                } else {
                    _uiState.value = PatientUiState.Error(response?.message() ?: "adding visit failed")
                }
            } catch (e: Exception) {
                _uiState.value = PatientUiState.Error(e.localizedMessage ?: "Error occurred")
            }
        }
    }
}
