package com.app.intellisoft.data.remote.models

data class AddVisitsRequestModel(
    val comments: String,
    val general_health: String,
    val on_diet: String,
    val on_drugs: String,
    val patient_id: String,
    val visit_date: String,
    val vital_id: String
)

data class AddVisitResponseModel(
    val code: Int,
    val `data`: AddVisitResponseData,
    val message: String,
    val success: Boolean
)

data class AddVisitResponseData(
    val message: String,
    val slug: Int
)