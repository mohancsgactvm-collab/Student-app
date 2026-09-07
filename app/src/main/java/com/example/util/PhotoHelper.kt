package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object PhotoHelper {

    fun saveUriToFile(context: Context, uri: Uri): String? {
        return try {
            val destinationFile = File(context.filesDir, "student_passport_photo_${System.currentTimeMillis()}.jpg")
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(destinationFile).use { output ->
                    input.copyTo(output)
                }
            }
            destinationFile.absolutePath
        } catch (_: Exception) {
            null
        }
    }

    fun saveBitmapToFile(context: Context, bitmap: Bitmap): String? {
        return try {
            val destinationFile = File(context.filesDir, "student_passport_photo_${System.currentTimeMillis()}.jpg")
            FileOutputStream(destinationFile).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            destinationFile.absolutePath
        } catch (_: Exception) {
            null
        }
    }
}
