package com.app.intellisoft.data.remote.models

import androidx.compose.ui.graphics.Color

data class AddVitalsRequestModel(
    val bmi: String,
    val height: String,
    val patient_id: String,
    val visit_date: String,
    val weight: String
)

data class AddVitalsResponseModel(
    val code: Int,
    val `data`: AddVitalsResponseData,
    val message: String,
    val success: Boolean
)

data class AddVitalsResponseData(
    val id: Int,
    val message: String,
    val patient_id: String,
    val slug: Int
)

data class BMICategory(
    val name: String,
    val emoji: String,
    val color: Color,
    val description: String
)