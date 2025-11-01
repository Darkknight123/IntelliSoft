package com.app.intellisoft.presentation.screens.patient

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.intellisoft.data.local.TokenManager
import com.app.intellisoft.data.remote.ApiClient
import com.app.intellisoft.data.remote.PatientService
import com.app.intellisoft.data.remote.models.AddVitalsRequestModel
import com.app.intellisoft.data.repository.PatientRepository
import com.app.intellisoft.presentation.viewmodel.PatientUiState
import com.app.intellisoft.presentation.viewmodel.PatientViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VitalsFormScreen(
    patientId: String,
    onContinueToAssessment: (bmi: Float, vitalId: Int) -> Unit
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager.getInstance(context) }
    val api = remember { ApiClient.retrofit.create(PatientService::class.java) }
    val repository = remember { PatientRepository(api, tokenManager) }
    val viewModel = remember { PatientViewModel(repository) }

    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    // --- Date Picker setup ---
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            selectedDate = "%02d/%02d/%d".format(dayOfMonth, month + 1, year)
        },
        calendar[Calendar.YEAR],
        calendar[Calendar.MONTH],
        calendar[Calendar.DAY_OF_MONTH]
    )

    // --- Auto-calculate BMI ---
    val bmi = remember(height, weight) {
        val h = height.toFloatOrNull()
        val w = weight.toFloatOrNull()
        if (h != null && w != null && h > 0)
            String.format("%.1f", w / ((h / 100) * (h / 100)))
        else ""
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Vitals Form") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Patient ID: $patientId", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() }
            ) {
                OutlinedTextField(
                    value = selectedDate,
                    onValueChange = {},
                    label = { Text("Visit Date *") },
                    readOnly = true,
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = height,
                onValueChange = { height = it },
                label = { Text("Height (CM) *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = weight,
                onValueChange = { weight = it },
                label = { Text("Weight (KG) *") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- BMI Card ---
            BMICard(bmiValue = bmi)

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    val bmiValue = bmi.toFloatOrNull()
                    if (bmiValue != null && height.isNotEmpty() && weight.isNotEmpty() && selectedDate.isNotEmpty()) {
                        val request = AddVitalsRequestModel(
                            bmi = bmi,
                            height = height,
                            weight = weight,
                            patient_id = patientId,
                            visit_date = selectedDate
                        )

                        viewModel.addVitals(request) {
                            val response = (uiState as? PatientUiState.VitalsSuccess)?.vitals
                            val vitalId: Int = response?.id ?: 0
                            onContinueToAssessment(bmiValue, vitalId)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save & Continue to Assessment")
            }

            Spacer(Modifier.height(16.dp))

            when (uiState) {
                is PatientUiState.Loading -> Text("Saving vitals...")
                is PatientUiState.Error -> Text("Error: ${(uiState as PatientUiState.Error).message}")
                is PatientUiState.Success -> Text("Vitals saved successfully!")
                else -> {}
            }
        }
    }
}

@Composable
fun BMICard(bmiValue: String) {
    if (bmiValue.isEmpty()) return

    val bmi = bmiValue.toFloatOrNull() ?: return

    val (category, color) = when {
        bmi < 18.5 -> "Underweight" to Color(0xFF64B5F6)
        bmi in 18.5..24.9 -> "Normal" to Color(0xFF81C784)
        bmi in 25.0..29.9 -> "Overweight" to Color(0xFFFFB74D)
        else -> "Obese" to Color(0xFFE57373)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "BMI: $bmiValue",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = category,
                color = color,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
