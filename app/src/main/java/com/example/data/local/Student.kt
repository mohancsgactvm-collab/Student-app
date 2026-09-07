package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a student's personal academic record (Biodata).
 * Digitizes the physical college "Student's Academic Record" register.
 *
 * Designed for V1 (Personal Record Module).
 *
 * ==============================================================================
 * FUTURE EXPANSION HOOKS (V2+ Architecture Guide):
 * ==============================================================================
 * 1. Semester Marks / Academic Records:
 *    Add related entity:
 *    @Entity(
 *        tableName = "academic_records",
 *        foreignKeys = [
 *            ForeignKey(
 *                entity = Student::class,
 *                parentColumns = ["id"],
 *                childColumns = ["studentId"],
 *                onDelete = ForeignKey.CASCADE
 *            )
 *        ]
 *    )
 *    data class AcademicRecord(
 *        @PrimaryKey(autoGenerate = true) val recordId: Long = 0,
 *        val studentId: Long,
 *        val semester: Int, // 1 to 8
 *        val subjectCode: String,
 *        val subjectName: String,
 *        val internalMarks: Double,
 *        val externalMarks: Double,
 *        val totalMarks: Double,
 *        val grade: String,
 *        val result: String // Pass / Fail
 *    )
 *
 * 2. Attendance Records:
 *    @Entity(
 *        tableName = "attendance_records",
 *        foreignKeys = [
 *            ForeignKey(
 *                entity = Student::class,
 *                parentColumns = ["id"],
 *                childColumns = ["studentId"],
 *                onDelete = ForeignKey.CASCADE
 *            )
 *        ]
 *    )
 *    data class AttendanceRecord(
 *        @PrimaryKey(autoGenerate = true) val recordId: Long = 0,
 *        val studentId: Long,
 *        val semester: Int,
 *        val month: String,
 *        val totalWorkingDays: Int,
 *        val daysAttended: Int,
 *        val percentage: Double
 *    )
 *
 * 3. Multi-Student Search & Roster:
 *    In V2, remove fixed id=1L constraint and set autoGenerate=true on [id],
 *    enabling full batch registration, semester-wise filtering, and roll number search.
 * ==============================================================================
 */
@Entity(tableName = "students")
data class Student(
    // In V1 single student mode, we default to id = 1L
    @PrimaryKey val id: Long = 1L,

    // --- Section A: Basic Information ---
    val nameEnglish: String,
    val nameTamil: String? = null,
    val rollNo: String,
    val admissionNo: String? = null,
    val courseAdmitted: String? = null,
    val dateOfAdmission: String? = null,
    val photoPath: String? = null,

    // --- Section B: Personal Details ---
    val dateOfBirth: String,
    val age: Int? = null,
    val community: String? = null,
    val subCaste: String? = null,
    val bloodGroup: String? = null,
    val aadharNumber: String? = null, // Stored strictly locally, masked in UI

    // --- Section C: Family & Contact ---
    val parentName: String? = null,
    val parentOccupation: String? = null,
    val residentialAddress: String? = null,
    val phoneNumber: String? = null,

    // --- Section D: Academic Background & Commute ---
    val permanentAddress: String? = null,
    val isDayscholar: Boolean = true, // true = Dayscholar, false = Hosteller
    val usesCollegeBus: Boolean = false,
    val schoolOrCollegeLastStudied: String? = null,
    val mediumOfStudy: String? = "English", // "English" or "Tamil"
    val percentageOfMarksScored: String? = null,
    val examMonthAndYear: String? = null,
    val registrationNumber: String? = null,

    // Metadata
    val updatedAt: Long = System.currentTimeMillis()
)
