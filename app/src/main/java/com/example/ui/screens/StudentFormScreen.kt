package com.example.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PhotoPickerSection
import com.example.ui.components.SectionCard
import com.example.ui.viewmodel.StudentRecordViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFormScreen(
    viewModel: StudentRecordViewModel,
    isEditMode: Boolean,
    onNavigateBack: () -> Unit,
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsState()
    val scrollState = rememberScrollState()

    var aadharMasked by remember { mutableStateOf(true) }

    // Dropdown expansion states
    var communityExpanded by remember { mutableStateOf(false) }
    var bloodGroupExpanded by remember { mutableStateOf(false) }
    var mediumExpanded by remember { mutableStateOf(false) }

    val communityList = listOf("OC", "BC", "BCM", "MBC/DNC", "SC", "SCA", "ST")
    val bloodGroupList = listOf("A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")
    val mediumList = listOf("English", "Tamil")

    // Date Picker Dialog for Date of Birth
    val calendar = Calendar.getInstance()
    val dobPickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            viewModel.onDateOfBirthSelected(year, month, dayOfMonth)
        },
        calendar.get(Calendar.YEAR) - 18,
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Date Picker Dialog for Date of Admission
    val admissionDatePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val formatted = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            viewModel.onDateOfAdmissionChange(formatted)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isEditMode) "Edit Student Record" else "New Student Record",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Register of Academic Record • Biodata",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("form_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Passport photo section
            PhotoPickerSection(
                photoPath = formState.photoPath,
                onPhotoSelected = { path -> viewModel.onPhotoPathChange(path) }
            )

            // -------------------------------------------------------------
            // SECTION A: BASIC INFO
            // -------------------------------------------------------------
            SectionCard(
                sectionLetter = "A",
                title = "Basic Information",
                subtitle = "Student identity, enrollment & course particulars"
            ) {
                // Name (English) [Required]
                OutlinedTextField(
                    value = formState.nameEnglish,
                    onValueChange = viewModel::onNameEnglishChange,
                    label = { Text("Name of the Student (in English) *") },
                    placeholder = { Text("e.g., S. SENTHIL KUMAR") },
                    isError = formState.nameEnglishError != null,
                    supportingText = {
                        formState.nameEnglishError?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("name_english_input")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Name (Tamil)
                OutlinedTextField(
                    value = formState.nameTamil,
                    onValueChange = viewModel::onNameTamilChange,
                    label = { Text("மாணவர் பெயர் (Name in Tamil)") },
                    placeholder = { Text("எ.கா. செ. செந்தில் குமார்") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("name_tamil_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Roll No & Admission No
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = formState.rollNo,
                        onValueChange = viewModel::onRollNoChange,
                        label = { Text("Roll No *") },
                        placeholder = { Text("e.g. 23CS045") },
                        isError = formState.rollNoError != null,
                        supportingText = {
                            formState.rollNoError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("roll_no_input")
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    OutlinedTextField(
                        value = formState.admissionNo,
                        onValueChange = viewModel::onAdmissionNoChange,
                        label = { Text("Admission No") },
                        placeholder = { Text("e.g. ADM/2023/112") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("admission_no_input")
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Course Admitted
                OutlinedTextField(
                    value = formState.courseAdmitted,
                    onValueChange = viewModel::onCourseAdmittedChange,
                    label = { Text("Course Admitted") },
                    placeholder = { Text("e.g. B.Sc. Computer Science") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("course_admitted_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Date of Admission
                OutlinedTextField(
                    value = formState.dateOfAdmission,
                    onValueChange = viewModel::onDateOfAdmissionChange,
                    label = { Text("Date of Admission") },
                    placeholder = { Text("DD/MM/YYYY") },
                    trailingIcon = {
                        IconButton(onClick = { admissionDatePickerDialog.show() }) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Pick Admission Date"
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("date_of_admission_input")
                )
            }

            // -------------------------------------------------------------
            // SECTION B: PERSONAL DETAILS
            // -------------------------------------------------------------
            SectionCard(
                sectionLetter = "B",
                title = "Personal Details",
                subtitle = "Biodata, birth details, community & identification"
            ) {
                // Date of Birth [Required] with auto-calculated age
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = formState.dateOfBirth,
                        onValueChange = viewModel::onDateOfBirthChange,
                        label = { Text("Date of Birth *") },
                        placeholder = { Text("DD/MM/YYYY") },
                        isError = formState.dobError != null,
                        supportingText = {
                            formState.dobError?.let {
                                Text(text = it, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        trailingIcon = {
                            IconButton(onClick = { dobPickerDialog.show() }) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Pick Date of Birth"
                                )
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1.4f)
                            .testTag("dob_input")
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Auto-calculated Age Display
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                        modifier = Modifier
                            .weight(0.9f)
                            .height(56.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(
                                text = "CALCULATED AGE",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = formState.age?.let { "$it Yrs" } ?: "--",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Community & Sub Caste
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Community Dropdown
                    ExposedDropdownMenuBox(
                        expanded = communityExpanded,
                        onExpandedChange = { communityExpanded = !communityExpanded },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = formState.community,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Community") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = communityExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .testTag("community_dropdown")
                        )
                        ExposedDropdownMenu(
                            expanded = communityExpanded,
                            onDismissRequest = { communityExpanded = false }
                        ) {
                            communityList.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        viewModel.onCommunityChange(item)
                                        communityExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Sub Caste
                    OutlinedTextField(
                        value = formState.subCaste,
                        onValueChange = viewModel::onSubCasteChange,
                        label = { Text("Sub Caste") },
                        placeholder = { Text("Optional") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("sub_caste_input")
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Blood Group Dropdown
                ExposedDropdownMenuBox(
                    expanded = bloodGroupExpanded,
                    onExpandedChange = { bloodGroupExpanded = !bloodGroupExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = formState.bloodGroup,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Blood Group") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodGroupExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("blood_group_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = bloodGroupExpanded,
                        onDismissRequest = { bloodGroupExpanded = false }
                    ) {
                        bloodGroupList.forEach { bg ->
                            DropdownMenuItem(
                                text = { Text(bg) },
                                onClick = {
                                    viewModel.onBloodGroupChange(bg)
                                    bloodGroupExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Aadhar Number (Masked input, 12 digits)
                OutlinedTextField(
                    value = formState.aadharNumber,
                    onValueChange = viewModel::onAadharChange,
                    label = { Text("Aadhar Number (12 digits)") },
                    placeholder = { Text("XXXXXXXXXXXX") },
                    visualTransformation = if (aadharMasked) PasswordVisualTransformation('•') else VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = formState.aadharError != null,
                    supportingText = {
                        if (formState.aadharError != null) {
                            Text(text = formState.aadharError!!, color = MaterialTheme.colorScheme.error)
                        } else {
                            Text(
                                text = "Digits entered: ${formState.aadharNumber.length}/12 • Stored locally only",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    trailingIcon = {
                        IconButton(onClick = { aadharMasked = !aadharMasked }) {
                            Icon(
                                imageVector = if (aadharMasked) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (aadharMasked) "Reveal Aadhar" else "Hide Aadhar"
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("aadhar_input")
                )
            }

            // -------------------------------------------------------------
            // SECTION C: FAMILY & CONTACT
            // -------------------------------------------------------------
            SectionCard(
                sectionLetter = "C",
                title = "Family & Contact Details",
                subtitle = "Parent/Guardian records and communication"
            ) {
                // Parent's Name
                OutlinedTextField(
                    value = formState.parentName,
                    onValueChange = viewModel::onParentNameChange,
                    label = { Text("Parent's / Guardian's Name") },
                    placeholder = { Text("e.g. M. Sivakumar") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("parent_name_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Parent's Occupation
                OutlinedTextField(
                    value = formState.parentOccupation,
                    onValueChange = viewModel::onParentOccupationChange,
                    label = { Text("Parent's / Guardian's Occupation") },
                    placeholder = { Text("e.g. Agriculture / Teacher / Business") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("parent_occupation_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Residential Address (multi-line)
                OutlinedTextField(
                    value = formState.residentialAddress,
                    onValueChange = viewModel::onResidentialAddressChange,
                    label = { Text("Residential Address") },
                    placeholder = { Text("Door No, Street Name, Village/City, District & PIN") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("residential_address_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Phone Number (Office/Res)
                OutlinedTextField(
                    value = formState.phoneNumber,
                    onValueChange = viewModel::onPhoneChange,
                    label = { Text("Phone Number (Office / Res)") },
                    placeholder = { Text("e.g. 9876543210") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = formState.phoneError != null,
                    supportingText = {
                        formState.phoneError?.let {
                            Text(text = it, color = MaterialTheme.colorScheme.error)
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("phone_input")
                )
            }

            // -------------------------------------------------------------
            // SECTION D: ACADEMIC BACKGROUND
            // -------------------------------------------------------------
            SectionCard(
                sectionLetter = "D",
                title = "Academic Background & Commute",
                subtitle = "Prior qualification, commute mode and permanent address"
            ) {
                // Permanent Address with copy residential action
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Permanent Address",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    SuggestionChip(
                        onClick = viewModel::copyResidentialToPermanent,
                        label = { Text("Same as Residential", fontSize = 11.sp) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = null,
                                modifier = Modifier.padding(end = 2.dp)
                            )
                        }
                    )
                }

                OutlinedTextField(
                    value = formState.permanentAddress,
                    onValueChange = viewModel::onPermanentAddressChange,
                    placeholder = { Text("Permanent address as per records") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("permanent_address_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dayscholar / Hosteller Toggle
                Text(
                    text = "Residential Status (Dayscholar / Hosteller)",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = formState.isDayscholar,
                        onClick = { viewModel.onDayscholarToggle(true) },
                        label = { Text("Dayscholar") },
                        leadingIcon = if (formState.isDayscholar) {
                            { Icon(Icons.Default.Check, contentDescription = null) }
                        } else null,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("toggle_dayscholar")
                    )

                    FilterChip(
                        selected = !formState.isDayscholar,
                        onClick = { viewModel.onDayscholarToggle(false) },
                        label = { Text("Hosteller") },
                        leadingIcon = if (!formState.isDayscholar) {
                            { Icon(Icons.Default.Home, contentDescription = null) }
                        } else null,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("toggle_hosteller")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // College Bus Toggle
                Text(
                    text = "Availing College Bus?",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = formState.usesCollegeBus,
                        onClick = { viewModel.onCollegeBusToggle(true) },
                        label = { Text("Yes (College Bus)") },
                        leadingIcon = {
                            Icon(Icons.Default.DirectionsBus, contentDescription = null)
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("toggle_bus_yes")
                    )

                    FilterChip(
                        selected = !formState.usesCollegeBus,
                        onClick = { viewModel.onCollegeBusToggle(false) },
                        label = { Text("No") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("toggle_bus_no")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // School / College Last Studied
                OutlinedTextField(
                    value = formState.schoolOrCollegeLastStudied,
                    onValueChange = viewModel::onSchoolOrCollegeLastStudiedChange,
                    label = { Text("Name of School / College Last Studied") },
                    placeholder = { Text("e.g. Govt Higher Secondary School, Salem") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("school_last_studied_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Medium of Study Dropdown
                ExposedDropdownMenuBox(
                    expanded = mediumExpanded,
                    onExpandedChange = { mediumExpanded = !mediumExpanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = formState.mediumOfStudy,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Medium of Study") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = mediumExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("medium_of_study_dropdown")
                    )
                    ExposedDropdownMenu(
                        expanded = mediumExpanded,
                        onDismissRequest = { mediumExpanded = false }
                    ) {
                        mediumList.forEach { m ->
                            DropdownMenuItem(
                                text = { Text(m) },
                                onClick = {
                                    viewModel.onMediumOfStudyChange(m)
                                    mediumExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Percentage of Marks Scored
                OutlinedTextField(
                    value = formState.percentageOfMarksScored,
                    onValueChange = viewModel::onPercentageOfMarksChange,
                    label = { Text("Percentage of Marks Scored (%)") },
                    placeholder = { Text("e.g. 88.5%") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("marks_percentage_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Exam Month & Year and Registration Number
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = formState.examMonthAndYear,
                        onValueChange = viewModel::onExamMonthAndYearChange,
                        label = { Text("Exam Month & Year") },
                        placeholder = { Text("e.g. March 2023") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("exam_month_year_input")
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    OutlinedTextField(
                        value = formState.registrationNumber,
                        onValueChange = viewModel::onRegistrationNumberChange,
                        label = { Text("Registration No") },
                        placeholder = { Text("e.g. 6112023") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("registration_number_input")
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Save & Submit Button
            Button(
                onClick = {
                    viewModel.saveStudent(onSuccess = onSaveSuccess)
                },
                enabled = !formState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_student_button"),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (formState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEditMode) "Save Changes" else "Save Student Record",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Cancel Button
            OutlinedButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("cancel_form_button"),
                shape = RoundedCornerShape(100.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Text(
                    text = "Cancel",
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
