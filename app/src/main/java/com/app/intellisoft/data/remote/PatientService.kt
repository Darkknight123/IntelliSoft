package com.app.intellisoft.data.remote

import com.app.intellisoft.data.remote.models.AddVisitResponseModel
import com.app.intellisoft.data.remote.models.AddVisitsRequestModel
import com.app.intellisoft.data.remote.models.AddVitalsRequestModel
import com.app.intellisoft.data.remote.models.AddVitalsResponseModel
import com.app.intellisoft.data.remote.models.LoginRequest
import com.app.intellisoft.data.remote.models.LoginResponse
import com.app.intellisoft.data.remote.models.Patient
import com.app.intellisoft.data.remote.models.PatientRequest
import com.app.intellisoft.data.remote.models.PatientResponse
import com.app.intellisoft.data.remote.models.RegisterRequest
import com.app.intellisoft.data.remote.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface PatientService {
    @Headers("Accept: application/json")
    @POST("patients/register")
    suspend fun registerPatient(
        @Header("Authorization") token: String,
        @Body patient: PatientRequest
    ): Response<Patient>

    @Headers("Accept: application/json")
    @GET("patients/view")
    suspend fun getPatients(
        @Header("Authorization") token: String
    ): Response<PatientResponse>

    @Headers("Accept: application/json")
    @POST("vital/add")
    suspend fun addVitals(
        @Header("Authorization") token: String,
        @Body patient: AddVitalsRequestModel
    ): Response<AddVitalsResponseModel>

    @Headers("Accept: application/json")
    @POST("visits/add")
    suspend fun addVisit(
        @Header("Authorization") token: String,
        @Body patient: AddVisitsRequestModel
    ): Response<AddVisitResponseModel>
}