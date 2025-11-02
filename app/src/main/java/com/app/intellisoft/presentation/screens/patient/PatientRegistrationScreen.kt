package com.app.intellisoft.presentation.screens.patient

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.intellisoft.data.local.TokenManager
import com.app.intellisoft.data.remote.ApiClient
import com.app.intellisoft.data.remote.PatientService
import com.app.intellisoft.data.remote.models.PatientRequest
import com.app.intellisoft.data.repository.PatientRepository
import com.app.intellisoft.presentation.viewmodel.PatientUiState
import com.app.intellisoft.presentation.viewmodel.PatientViewModel
import java.util.Calendar

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

    val uiState by viewModel.uiState.collectAsState()

    var patientId by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }

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


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Patient Registration",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(Modifier.height(16.dp))

                // Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.secondary
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👤", fontSize = 28.sp)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                "New Patient",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                "Fill in patient details",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Form Fields
                ImprovedOutlinedTextField(
                    value = patientId,
                    onValueChange = { patientId = it },
                    label = "Patient ID *",
                    emoji = "🆔"
                )

                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() }
                ) {
                    ImprovedOutlinedTextField(
                        value = selectedDate,
                        onValueChange = {selectedDate = it},
                        label = "Registration Date *",
                        emoji = "📅",
                        readOnly = true,
                        enabled = false
                    )
                }

                Spacer(Modifier.height(16.dp))

                ImprovedOutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = "First Name *",
                    emoji = "✏️"
                )

                Spacer(Modifier.height(16.dp))

                ImprovedOutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = "Last Name *",
                    emoji = "✏️"
                )

                Spacer(Modifier.height(16.dp))


                ImprovedOutlinedTextField(
                    value = dob,
                    onValueChange = { dob = it },
                    label = "Date of Birth *",
                    emoji = "🎂"
                )

                Spacer(Modifier.height(16.dp))

                // Gender Dropdown
                var expanded by remember { mutableStateOf(false) }

                Text(
                    "Gender *",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = gender,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Select Gender") },
                        trailingIcon = {
                            Text(if (expanded) "🔼" else "🔽", fontSize = 16.sp)
                        },
                        leadingIcon = {
                            Text(
                                when (gender) {
                                    "Male" -> "👨"
                                    "Female" -> "👩"
                                    else -> "⚧️"
                                },
                                fontSize = 20.sp
                            )
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("Male" to "👨", "Female" to "👩", "Other" to "⚧️").forEach { (text, emoji) ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(emoji, fontSize = 20.sp)
                                        Text(text)
                                    }
                                },
                                onClick = {
                                    gender = text
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Status Messages
                when (uiState) {
                    is PatientUiState.Loading -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(Modifier.width(16.dp))
                                Text("Saving patient...", fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                    is PatientUiState.Error -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("❌", fontSize = 24.sp)
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    "Error: ${(uiState as PatientUiState.Error).message}",
                                    color = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                    else -> {}
                }

                Spacer(Modifier.height(24.dp))
            }

            // Bottom Button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(56.dp),
                    enabled = patientId.isNotEmpty() && firstName.isNotEmpty() && gender.isNotEmpty(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(
                        "Save & Continue to Vitals",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ImprovedOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    emoji: String,
    readOnly: Boolean = false,
    enabled: Boolean = true
) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = { Text(emoji, fontSize = 20.sp) },
            readOnly = readOnly,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        )
    }
}