package com.app.intellisoft.data.repository

import android.util.Log
import com.app.intellisoft.data.local.TokenManager
import com.app.intellisoft.data.remote.PatientService
import com.app.intellisoft.data.remote.models.AddVisitsRequestModel
import com.app.intellisoft.data.remote.models.AddVitalsRequestModel
import com.app.intellisoft.data.remote.models.PatientRequest
import com.app.intellisoft.data.remote.models.PatientResponse
import com.app.intellisoft.data.remote.models.Patient
import retrofit2.Response

class PatientRepository(
    private val api: PatientService,
    private val tokenManager: TokenManager
) {
    suspend fun registerPatient(patient: PatientRequest) =
        tokenManager.getToken()?.let {
            api.registerPatient("Bearer $it", patient)
        }

    suspend fun getPatients() =
        tokenManager.getToken()?.let {
            api.getPatients("Bearer $it")
        }

    suspend fun addVitals(vitals: AddVitalsRequestModel) =
        tokenManager.getToken()?.let {
            api.addVitals("Bearer $it", vitals)
        }

    suspend fun addVisit(visit: AddVisitsRequestModel) =
        tokenManager.getToken()?.let {
            api.addVisit("Bearer $it", visit)
        }
}
