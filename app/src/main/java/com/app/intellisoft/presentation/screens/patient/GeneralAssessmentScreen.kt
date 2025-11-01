package com.app.intellisoft.presentation.screens.patient

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.intellisoft.data.local.TokenManager
import com.app.intellisoft.data.remote.ApiClient
import com.app.intellisoft.data.remote.PatientService
import com.app.intellisoft.data.remote.models.AddVisitsRequestModel
import com.app.intellisoft.data.repository.PatientRepository
import com.app.intellisoft.presentation.viewmodel.PatientUiState
import com.app.intellisoft.presentation.viewmodel.PatientViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentScreen(
    type: AssessmentType,
    patientId: String,
    vitalId: String,
    onBack: () -> Unit = {},
    onSubmitSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager.getInstance(context) }
    val api = remember { ApiClient.retrofit.create(PatientService::class.java) }
    val repository = remember { PatientRepository(api, tokenManager) }
    val viewModel = remember { PatientViewModel(repository) }

    var generalHealth by remember { mutableStateOf("") }
    var secondaryQuestion by remember { mutableStateOf("") }
    var comments by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }

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

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (type) {
                            AssessmentType.General -> "General Assessment"
                            AssessmentType.Overweight -> "Overweight Assessment"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) { /* Back icon here */ }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00796B),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() } // Handle click here
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

            Spacer(Modifier.height(16.dp))

            // General Health selection
            Text("General Health *", fontWeight = FontWeight.SemiBold)
            listOf("Good", "Poor").forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = generalHealth == option,
                            onClick = { generalHealth = option }
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (generalHealth == option),
                        onClick = { generalHealth = option }
                    )
                    Text(option, modifier = Modifier.padding(start = 8.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            // Conditional question
            val secondaryLabel = when (type) {
                AssessmentType.General -> "Have you ever been on a diet to lose weight? *"
                AssessmentType.Overweight -> "Are you currently using any drugs? *"
            }

            Text(secondaryLabel, fontWeight = FontWeight.SemiBold)

            listOf("Yes", "No").forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = secondaryQuestion == option,
                            onClick = { secondaryQuestion = option }
                        )
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (secondaryQuestion == option),
                        onClick = { secondaryQuestion = option }
                    )
                    Text(option, modifier = Modifier.padding(start = 8.dp))
                }
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = comments,
                onValueChange = { comments = it },
                label = { Text("Comments") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (selectedDate.isNotEmpty() && generalHealth.isNotEmpty()) {
                        val request = AddVisitsRequestModel(
                            comments = comments,
                            general_health = generalHealth,
                            on_diet = if (type == AssessmentType.General) secondaryQuestion else "NA",
                            on_drugs = if (type == AssessmentType.Overweight) secondaryQuestion else "NA",
                            patient_id = patientId,
                            visit_date = selectedDate,
                            vital_id = vitalId
                        )
                        viewModel.addVisit(request) {
                            onSubmitSuccess()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB0BEC5),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Submit Assessment", fontSize = 16.sp)
            }

            Spacer(Modifier.height(16.dp))

            when (uiState) {
                is PatientUiState.Loading -> Text("Submitting assessment...")
                is PatientUiState.Error -> Text("Error: ${(uiState as PatientUiState.Error).message}")
                else -> {}
            }
        }
    }
}


enum class AssessmentType {
    General, Overweight
}

