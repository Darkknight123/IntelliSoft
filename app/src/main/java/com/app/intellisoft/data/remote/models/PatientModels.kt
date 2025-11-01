package com.app.intellisoft.data.remote.models

data class PatientRequest(
    val firstname: String,
    val lastname: String,
    val unique: String,
    val dob: String,
    val gender: String,
    val reg_date: String
)

data class Patient(
    val id: Int,
    val unique: String,
    val firstname: String,
    val lastname: String,
    val dob: String,
    val gender: String,
    val reg_date: String,
    val created_at: String,
    val updated_at: String
)

data class PatientResponse(
    val message: String,
    val success: Boolean,
    val code: Int,
    val data: List<Patient>
)