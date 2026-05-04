package com.example.pruebascopilot

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.runner.Description
import org.junit.runner.Result
import org.junit.runner.notification.RunListener

class TestListener : RunListener() {

    override fun testRunStarted(description: Description?) {
        super.testRunStarted(description)
        requestStoragePermissions()
        ensureDirectories()
    }

    private fun requestStoragePermissions() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val pm = context.packageManager
        val packageName = context.packageName
        val writeStoragePermissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Para Android 11 y superior, verificar si tenemos permisos
            for (permission in writeStoragePermissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    grantPermissionViaShell(permission, packageName)
                }
            }
        }
    }

    private fun grantPermissionViaShell(permission: String, packageName: String) {
        try {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            instrumentation.uiAutomation.executeShellCommand("pm grant $packageName $permission")
            Thread.sleep(500)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun ensureDirectories() {
        try {
            val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
            val context = InstrumentationRegistry.getInstrumentation().targetContext

            // Crear directorios necesarios
            val screenshotsDir = "/sdcard/screenshots"
            val allureResultsDir = "/sdcard/Android/data/${context.packageName}/cache/allure-results"

            device.executeShellCommand("mkdir -p $screenshotsDir")
            device.executeShellCommand("mkdir -p $allureResultsDir")
            device.executeShellCommand("chmod 777 $screenshotsDir")
            device.executeShellCommand("chmod 777 $allureResultsDir")

            Thread.sleep(500)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

