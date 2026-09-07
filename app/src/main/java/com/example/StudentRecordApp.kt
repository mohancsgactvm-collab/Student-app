package com.example

import android.app.Application
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.StudentFormScreen
import com.example.ui.screens.StudentProfileScreen
import com.example.ui.viewmodel.StudentRecordViewModel

enum class AppScreen {
    HOME,
    FORM,
    PROFILE
}

@Composable
fun StudentRecordApp() {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: StudentRecordViewModel = viewModel(
        factory = StudentRecordViewModel.provideFactory(application)
    )

    val currentStudent by viewModel.currentStudent.collectAsState()

    var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
    var isEditMode by remember { mutableStateOf(false) }

    when (currentScreen) {
        AppScreen.HOME -> {
            HomeScreen(
                student = currentStudent,
                onOpenProfile = {
                    currentScreen = AppScreen.PROFILE
                },
                onAddNewStudent = {
                    viewModel.initFormForEdit(null)
                    isEditMode = false
                    currentScreen = AppScreen.FORM
                },
                onEditStudent = {
                    viewModel.initFormForEdit(currentStudent)
                    isEditMode = true
                    currentScreen = AppScreen.FORM
                }
            )
        }

        AppScreen.FORM -> {
            BackHandler {
                currentScreen = if (currentStudent != null) AppScreen.PROFILE else AppScreen.HOME
            }
            StudentFormScreen(
                viewModel = viewModel,
                isEditMode = isEditMode,
                onNavigateBack = {
                    currentScreen = if (currentStudent != null) AppScreen.PROFILE else AppScreen.HOME
                },
                onSaveSuccess = {
                    currentScreen = AppScreen.PROFILE
                }
            )
        }

        AppScreen.PROFILE -> {
            BackHandler {
                currentScreen = AppScreen.HOME
            }
            if (currentStudent != null) {
                StudentProfileScreen(
                    student = currentStudent!!,
                    onEditClick = {
                        viewModel.initFormForEdit(currentStudent)
                        isEditMode = true
                        currentScreen = AppScreen.FORM
                    },
                    onNavigateBack = {
                        currentScreen = AppScreen.HOME
                    },
                    onDeleteClick = {
                        viewModel.deleteStudentRecord {
                            currentScreen = AppScreen.HOME
                        }
                    }
                )
            } else {
                // If student was deleted or null, fall back to home
                HomeScreen(
                    student = null,
                    onOpenProfile = {},
                    onAddNewStudent = {
                        viewModel.initFormForEdit(null)
                        isEditMode = false
                        currentScreen = AppScreen.FORM
                    },
                    onEditStudent = {}
                )
            }
        }
    }
}
