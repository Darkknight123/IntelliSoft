package com.app.intellisoft.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.intellisoft.presentation.screens.onboarding.LoginScreen
import com.app.intellisoft.presentation.screens.onboarding.RegisterScreen
import com.app.intellisoft.presentation.screens.patient.AssessmentScreen
import com.app.intellisoft.presentation.screens.patient.AssessmentType
import com.app.intellisoft.presentation.screens.patient.PatientListingScreen
import com.app.intellisoft.presentation.screens.patient.PatientRegistrationScreen
import com.app.intellisoft.presentation.screens.patient.VitalsFormScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Signup.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(onLoginSuccess = { navController.navigate(Screen.PatientList.route) })
        }
        composable(Screen.Signup.route) {
            RegisterScreen(onSignupSuccess = { navController.navigate(Screen.Login.route) })
        }

        composable(Screen.PatientList.route) {
            PatientListingScreen(
                onAddPatient = { navController.navigate(Screen.PatientRegistration.route) },
                onPatientClick = { id -> navController.navigate(Screen.VitalsForm.createRoute(id)) }
            )
        }

        composable(Screen.PatientRegistration.route) {
            PatientRegistrationScreen(onContinue = { patientId ->
                navController.navigate(Screen.VitalsForm.createRoute(patientId))
            })
        }

        composable(
            Screen.VitalsForm.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            VitalsFormScreen(
                patientId = patientId,
                onContinueToAssessment = { bmi, vitalId ->
                    if (bmi <= 25) {
                        navController.navigate(Screen.GeneralAssessment.createRoute(patientId, vitalId.toString()))
                    } else {
                        navController.navigate(Screen.OverweightAssessment.createRoute(patientId, vitalId.toString()))
                    }
                }
            )
        }

        composable(
            Screen.GeneralAssessment.route,
            arguments = listOf(
                navArgument("patientId") { type = NavType.StringType },
                navArgument("vitalId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            val vitalId = backStackEntry.arguments?.getString("vitalId") ?: ""
            AssessmentScreen(
                type = AssessmentType.General,
                onBack = { navController.popBackStack() },
                onSubmitSuccess = {
                    navController.navigate(Screen.PatientList.route)
                },
                patientId = patientId,
                vitalId = vitalId
            )
        }

        composable(
            Screen.OverweightAssessment.route,
            arguments = listOf(
                navArgument("patientId") { type = NavType.StringType },
                navArgument("vitalId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            val vitalId = backStackEntry.arguments?.getString("vitalId") ?: ""
            AssessmentScreen(
                type = AssessmentType.Overweight,
                onBack = { navController.popBackStack() },
                onSubmitSuccess = {
                    navController.navigate(Screen.PatientList.route)
                },
                patientId = patientId,
                vitalId = vitalId
            )
        }

    }
}


