package com.app.intellisoft.presentation.navigation

sealed class Screen(val route: String) {
    object PatientList : Screen("patient_list")
    object PatientRegistration : Screen("patient_registration")

    object VitalsForm : Screen("vitals_form/{patientId}") {
        fun createRoute(patientId: String) = "vitals_form/$patientId"
    }

    object GeneralAssessment : Screen("general_assessment/{patientId}/{vitalId}") {
        fun createRoute(patientId: String, vitalId: String) =
            "general_assessment/$patientId/$vitalId"
    }

    object OverweightAssessment : Screen("assessment/overweight/{patientId}/{vitalId}") {
        fun createRoute(patientId: String, vitalId: String) =
            "assessment/overweight/$patientId/$vitalId"
    }

    object Login : Screen("login")
    object Signup : Screen("signup")
}
