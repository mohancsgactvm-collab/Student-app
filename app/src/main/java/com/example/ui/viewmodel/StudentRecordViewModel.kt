package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.Student
import com.example.data.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

data class StudentFormState(
    // Section A
    val nameEnglish: String = "",
    val nameTamil: String = "",
    val rollNo: String = "",
    val admissionNo: String = "",
    val courseAdmitted: String = "",
    val dateOfAdmission: String = "",
    val photoPath: String? = null,

    // Section B
    val dateOfBirth: String = "",
    val age: Int? = null,
    val community: String = "BC",
    val subCaste: String = "",
    val bloodGroup: String = "O+",
    val aadharNumber: String = "", // 12 digits

    // Section C
    val parentName: String = "",
    val parentOccupation: String = "",
    val residentialAddress: String = "",
    val phoneNumber: String = "",

    // Section D
    val permanentAddress: String = "",
    val isDayscholar: Boolean = true,
    val usesCollegeBus: Boolean = false,
    val schoolOrCollegeLastStudied: String = "",
    val mediumOfStudy: String = "English",
    val percentageOfMarksScored: String = "",
    val examMonthAndYear: String = "",
    val registrationNumber: String = "",

    // Validation errors
    val nameEnglishError: String? = null,
    val rollNoError: String? = null,
    val dobError: String? = null,
    val aadharError: String? = null,
    val phoneError: String? = null,

    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

class StudentRecordViewModel(
    application: Application,
    private val repository: StudentRepository
) : AndroidViewModel(application) {

    val currentStudent: StateFlow<Student?> = repository.currentStudent
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _formState = MutableStateFlow(StudentFormState())
    val formState: StateFlow<StudentFormState> = _formState.asStateFlow()

    fun initFormForEdit(student: Student?) {
        if (student != null) {
            _formState.value = StudentFormState(
                nameEnglish = student.nameEnglish,
                nameTamil = student.nameTamil.orEmpty(),
                rollNo = student.rollNo,
                admissionNo = student.admissionNo.orEmpty(),
                courseAdmitted = student.courseAdmitted.orEmpty(),
                dateOfAdmission = student.dateOfAdmission.orEmpty(),
                photoPath = student.photoPath,
                dateOfBirth = student.dateOfBirth,
                age = student.age ?: calculateAgeFromDate(student.dateOfBirth),
                community = student.community ?: "BC",
                subCaste = student.subCaste.orEmpty(),
                bloodGroup = student.bloodGroup ?: "O+",
                aadharNumber = student.aadharNumber.orEmpty(),
                parentName = student.parentName.orEmpty(),
                parentOccupation = student.parentOccupation.orEmpty(),
                residentialAddress = student.residentialAddress.orEmpty(),
                phoneNumber = student.phoneNumber.orEmpty(),
                permanentAddress = student.permanentAddress.orEmpty(),
                isDayscholar = student.isDayscholar,
                usesCollegeBus = student.usesCollegeBus,
                schoolOrCollegeLastStudied = student.schoolOrCollegeLastStudied.orEmpty(),
                mediumOfStudy = student.mediumOfStudy ?: "English",
                percentageOfMarksScored = student.percentageOfMarksScored.orEmpty(),
                examMonthAndYear = student.examMonthAndYear.orEmpty(),
                registrationNumber = student.registrationNumber.orEmpty()
            )
        } else {
            _formState.value = StudentFormState()
        }
    }

    // --- Section A Field Updates ---
    fun onNameEnglishChange(value: String) {
        _formState.update { it.copy(nameEnglish = value, nameEnglishError = null) }
    }

    fun onNameTamilChange(value: String) {
        _formState.update { it.copy(nameTamil = value) }
    }

    fun onRollNoChange(value: String) {
        _formState.update { it.copy(rollNo = value.uppercase().trim(), rollNoError = null) }
    }

    fun onAdmissionNoChange(value: String) {
        _formState.update { it.copy(admissionNo = value.trim()) }
    }

    fun onCourseAdmittedChange(value: String) {
        _formState.update { it.copy(courseAdmitted = value) }
    }

    fun onDateOfAdmissionChange(value: String) {
        _formState.update { it.copy(dateOfAdmission = value) }
    }

    fun onPhotoPathChange(path: String?) {
        _formState.update { it.copy(photoPath = path) }
    }

    // --- Section B Field Updates ---
    fun onDateOfBirthChange(dateString: String) {
        val calculatedAge = calculateAgeFromDate(dateString)
        _formState.update {
            it.copy(
                dateOfBirth = dateString,
                age = calculatedAge,
                dobError = null
            )
        }
    }

    fun onDateOfBirthSelected(year: Int, month: Int, dayOfMonth: Int) {
        val formattedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
        val calculatedAge = calculateAge(year, month, dayOfMonth)
        _formState.update {
            it.copy(
                dateOfBirth = formattedDate,
                age = calculatedAge,
                dobError = null
            )
        }
    }

    fun onCommunityChange(value: String) {
        _formState.update { it.copy(community = value) }
    }

    fun onSubCasteChange(value: String) {
        _formState.update { it.copy(subCaste = value) }
    }

    fun onBloodGroupChange(value: String) {
        _formState.update { it.copy(bloodGroup = value) }
    }

    fun onAadharChange(rawInput: String) {
        // Keep only digits, maximum 12
        val digitsOnly = rawInput.filter { it.isDigit() }.take(12)
        _formState.update { it.copy(aadharNumber = digitsOnly, aadharError = null) }
    }

    // --- Section C Field Updates ---
    fun onParentNameChange(value: String) {
        _formState.update { it.copy(parentName = value) }
    }

    fun onParentOccupationChange(value: String) {
        _formState.update { it.copy(parentOccupation = value) }
    }

    fun onResidentialAddressChange(value: String) {
        _formState.update { it.copy(residentialAddress = value) }
    }

    fun onPhoneChange(value: String) {
        _formState.update { it.copy(phoneNumber = value, phoneError = null) }
    }

    // --- Section D Field Updates ---
    fun onPermanentAddressChange(value: String) {
        _formState.update { it.copy(permanentAddress = value) }
    }

    fun copyResidentialToPermanent() {
        _formState.update { it.copy(permanentAddress = it.residentialAddress) }
    }

    fun onDayscholarToggle(isDayscholar: Boolean) {
        _formState.update { it.copy(isDayscholar = isDayscholar) }
    }

    fun onCollegeBusToggle(usesBus: Boolean) {
        _formState.update { it.copy(usesCollegeBus = usesBus) }
    }

    fun onSchoolOrCollegeLastStudiedChange(value: String) {
        _formState.update { it.copy(schoolOrCollegeLastStudied = value) }
    }

    fun onMediumOfStudyChange(value: String) {
        _formState.update { it.copy(mediumOfStudy = value) }
    }

    fun onPercentageOfMarksChange(value: String) {
        _formState.update { it.copy(percentageOfMarksScored = value) }
    }

    fun onExamMonthAndYearChange(value: String) {
        _formState.update { it.copy(examMonthAndYear = value) }
    }

    fun onRegistrationNumberChange(value: String) {
        _formState.update { it.copy(registrationNumber = value) }
    }

    // --- Validation & Submission ---
    private fun validate(): Boolean {
        val state = _formState.value
        var isValid = true

        val nameError = if (state.nameEnglish.trim().isEmpty()) {
            isValid = false
            "Student name is required"
        } else null

        val rollError = if (state.rollNo.trim().isEmpty()) {
            isValid = false
            "Roll number is required"
        } else null

        val dobError = if (state.dateOfBirth.trim().isEmpty()) {
            isValid = false
            "Date of birth is required"
        } else null

        val aadharError = if (state.aadharNumber.isNotEmpty() && state.aadharNumber.length != 12) {
            isValid = false
            "Aadhar number must be exactly 12 digits"
        } else null

        val phoneClean = state.phoneNumber.replace(" ", "").replace("-", "")
        val phoneError = if (phoneClean.isNotEmpty()) {
            val digitsOnly = phoneClean.filter { it.isDigit() }
            if (digitsOnly.length !in 10..12) {
                isValid = false
                "Phone must be a valid 10-digit number"
            } else null
        } else null

        _formState.update {
            it.copy(
                nameEnglishError = nameError,
                rollNoError = rollError,
                dobError = dobError,
                aadharError = aadharError,
                phoneError = phoneError
            )
        }

        return isValid
    }

    fun saveStudent(onSuccess: () -> Unit) {
        if (!validate()) return

        val state = _formState.value
        _formState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            val student = Student(
                id = 1L, // Single student record in V1
                nameEnglish = state.nameEnglish.trim(),
                nameTamil = state.nameTamil.trim().ifEmpty { null },
                rollNo = state.rollNo.trim(),
                admissionNo = state.admissionNo.trim().ifEmpty { null },
                courseAdmitted = state.courseAdmitted.trim().ifEmpty { null },
                dateOfAdmission = state.dateOfAdmission.trim().ifEmpty { null },
                photoPath = state.photoPath,
                dateOfBirth = state.dateOfBirth.trim(),
                age = state.age ?: calculateAgeFromDate(state.dateOfBirth),
                community = state.community.ifEmpty { null },
                subCaste = state.subCaste.trim().ifEmpty { null },
                bloodGroup = state.bloodGroup.ifEmpty { null },
                aadharNumber = state.aadharNumber.ifEmpty { null },
                parentName = state.parentName.trim().ifEmpty { null },
                parentOccupation = state.parentOccupation.trim().ifEmpty { null },
                residentialAddress = state.residentialAddress.trim().ifEmpty { null },
                phoneNumber = state.phoneNumber.trim().ifEmpty { null },
                permanentAddress = state.permanentAddress.trim().ifEmpty { null },
                isDayscholar = state.isDayscholar,
                usesCollegeBus = state.usesCollegeBus,
                schoolOrCollegeLastStudied = state.schoolOrCollegeLastStudied.trim().ifEmpty { null },
                mediumOfStudy = state.mediumOfStudy,
                percentageOfMarksScored = state.percentageOfMarksScored.trim().ifEmpty { null },
                examMonthAndYear = state.examMonthAndYear.trim().ifEmpty { null },
                registrationNumber = state.registrationNumber.trim().ifEmpty { null }
            )

            repository.saveStudent(student)
            _formState.update { it.copy(isSaving = false, saveSuccess = true) }
            onSuccess()
        }
    }

    fun deleteStudentRecord(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteStudent(1L)
            _formState.value = StudentFormState()
            onDeleted()
        }
    }

    companion object {
        fun calculateAge(year: Int, month: Int, dayOfMonth: Int): Int {
            val today = Calendar.getInstance()
            val dob = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--
            }
            return age.coerceAtLeast(0)
        }

        fun calculateAgeFromDate(dobString: String): Int? {
            return try {
                val cleaned = dobString.trim()
                val parts = cleaned.split("/", "-", ".")
                if (parts.size == 3) {
                    val (d, m, y) = if (parts[0].length == 4) {
                        Triple(parts[2].toInt(), parts[1].toInt() - 1, parts[0].toInt())
                    } else {
                        Triple(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
                    }
                    calculateAge(y, m, d)
                } else null
            } catch (_: Exception) {
                null
            }
        }

        fun provideFactory(application: Application): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                    val database = AppDatabase.getDatabase(application)
                    val repository = StudentRepository(database.studentDao())
                    return StudentRecordViewModel(application, repository) as T
                }
            }
        }
    }
}
