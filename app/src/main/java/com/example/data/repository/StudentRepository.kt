package com.example.data.repository

import com.example.data.local.Student
import com.example.data.local.StudentDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudentRepository(private val studentDao: StudentDao) {

    /**
     * Emits the single student record for V1 (id = 1L).
     */
    val currentStudent: Flow<Student?> = studentDao.getStudent(1L)

    /**
     * Checks if a record exists.
     */
    val hasStudentRecord: Flow<Boolean> = studentDao.getStudentCount().map { it > 0 }

    /**
     * Saves or updates the student's biodata.
     */
    suspend fun saveStudent(student: Student) {
        studentDao.insertOrUpdateStudent(student)
    }

    /**
     * Removes the student record.
     */
    suspend fun deleteStudent(id: Long = 1L) {
        studentDao.deleteStudent(id)
    }
}
