package com.app.intellisoft.presentation.screens.patient

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.app.intellisoft.data.local.TokenManager
import com.app.intellisoft.data.remote.ApiClient
import com.app.intellisoft.data.remote.PatientService
import com.app.intellisoft.data.remote.models.PatientRequest
import com.app.intellisoft.data.repository.AuthRepository
import com.app.intellisoft.data.repository.PatientRepository
import com.app.intellisoft.presentation.viewmodel.PatientUiState
import com.app.intellisoft.presentation.viewmodel.PatientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientRegistrationScreen(
    onContinue: (String) -> Unit
) {

    val context = LocalContext.current

    val tokenManager = remember { TokenManager.getInstance(context) }
    val api = remember { ApiClient.retrofit.create(PatientService::class.java) }
    val repository = remember { PatientRepository(api, tokenManager) }
    val viewModel = remember { PatientViewModel(repository) }


    // Observe UI state
    val uiState by viewModel.uiState.collectAsState()

    // Form state
    var patientId by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }


    Scaffold(
        topBar = { TopAppBar(title = { Text("Patient Registration") }) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(value = patientId, onValueChange = { patientId = it }, label = { Text("Patient ID *") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "2025-10-31", onValueChange = {}, label = { Text("Registration Date *") }, readOnly = true, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name *") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name *") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = dob, onValueChange = { dob = it }, label = { Text("Date of Birth *") }, modifier = Modifier.fillMaxWidth())

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender *") },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Male", "Female", "Other").forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = {
                            gender = it
                            expanded = false
                        })
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    val request = PatientRequest(
                        firstname = firstName,
                        lastname = lastName,
                        unique = patientId,
                        dob = dob,
                        gender = gender,
                        reg_date = "2025-10-31"
                    )
                    viewModel.registerPatient(request) {
                        onContinue(patientId)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = patientId.isNotEmpty() && firstName.isNotEmpty() && gender.isNotEmpty()
            ) {
                Text("Save & Continue to Vitals")
            }

            if (uiState is PatientUiState.Loading) {
                Text("Saving patient...", Modifier.padding(8.dp))
            }
            if (uiState is PatientUiState.Error) {
                Text("Error: ${(uiState as PatientUiState.Error).message}", Modifier.padding(8.dp))
            }
        }
    }
}

