package com.app.intellisoft.presentation.screens.patient

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
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

    val (headerColor, emoji, title) = when (type) {
        AssessmentType.General -> Triple(Color(0xFF4CAF50), "📋", "General Assessment")
        AssessmentType.Overweight -> Triple(Color(0xFFFF5722), "⚠️", "Overweight Assessment")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(emoji, fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                        Text(
                            title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("⬅️", fontSize = 24.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = headerColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
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

                // Patient Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = headerColor.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = Brush.horizontalGradient(
                            listOf(headerColor, headerColor.copy(alpha = 0.5f))
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                "Patient ID: $patientId",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = headerColor
                            )
                            Text(
                                when (type) {
                                    AssessmentType.General -> "BMI ≤ 25 (Normal Range)"
                                    AssessmentType.Overweight -> "BMI > 25 (Overweight)"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = headerColor
                        ) {
                            Text(
                                emoji,
                                fontSize = 32.sp,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Visit Date
                Text(
                    "Visit Date *",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() }
                ) {
                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = {},
                        leadingIcon = { Text("📅", fontSize = 20.sp) },
                        placeholder = { Text("Select visit date") },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            disabledTextColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Spacer(Modifier.height(24.dp))

                // General Health Question
                Text(
                    "General Health *",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                listOf("Good" to "😊", "Poor" to "😟").forEach { (option, emoji) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .selectable(
                                selected = generalHealth == option,
                                onClick = { generalHealth = option }
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (generalHealth == option)
                                headerColor.copy(alpha = 0.15f)
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = if (generalHealth == option)
                            CardDefaults.outlinedCardBorder().copy(width = 2.dp, brush = Brush.horizontalGradient(listOf(headerColor, headerColor)))
                        else
                            CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = generalHealth == option,
                                onClick = { generalHealth = option },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = headerColor
                                )
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(emoji, fontSize = 24.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                option,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (generalHealth == option) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Secondary Question
                val secondaryLabel = when (type) {
                    AssessmentType.General -> "Have you ever been on a diet to lose weight? *"
                    AssessmentType.Overweight -> "Are you currently using any drugs? *"
                }

                Text(
                    secondaryLabel,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                listOf("Yes" to "✅", "No" to "❌").forEach { (option, emoji) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .selectable(
                                selected = secondaryQuestion == option,
                                onClick = { secondaryQuestion = option }
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (secondaryQuestion == option)
                                headerColor.copy(alpha = 0.15f)
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = if (secondaryQuestion == option)
                            CardDefaults.outlinedCardBorder().copy(width = 2.dp, brush = Brush.horizontalGradient(listOf(headerColor, headerColor)))
                        else
                            CardDefaults.outlinedCardBorder()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = secondaryQuestion == option,
                                onClick = { secondaryQuestion = option },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = headerColor
                                )
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(emoji, fontSize = 24.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                option,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (secondaryQuestion == option) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Comments Field
                Text(
                    "Comments",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = comments,
                    onValueChange = { comments = it },
                    leadingIcon = { Text("💬", fontSize = 20.sp) },
                    placeholder = { Text("Enter any additional observations...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = headerColor,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                )

                Spacer(Modifier.height(16.dp))

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
                                Text("Submitting assessment...", fontWeight = FontWeight.Medium)
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
                        .padding(20.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = headerColor
                    )
                ) {
                    Text(
                        "Submit Assessment",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

enum class AssessmentType {
    General, Overweight
}

