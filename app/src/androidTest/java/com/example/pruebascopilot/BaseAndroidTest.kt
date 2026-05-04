package com.example.pruebascopilot

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import java.io.File

abstract class BaseAndroidTest {

    protected val device: UiDevice by lazy {
        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    protected val context: Context by lazy {
        InstrumentationRegistry.getInstrumentation().targetContext
    }

    protected fun ensurePermissions() {
        try {
            val instrumentation = InstrumentationRegistry.getInstrumentation()
            val packageName = context.packageName

            val permissions = arrayOf(
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.MANAGE_EXTERNAL_STORAGE"
            )

            for (permission in permissions) {
                try {
                    instrumentation.uiAutomation.executeShellCommand("pm grant $packageName $permission")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun createDirectoriesIfNeeded() {
        try {
            listOf(
                "/sdcard/screenshots",
                "/sdcard/Android/data/${context.packageName}/cache/allure-results"
            ).forEach { dir ->
                device.executeShellCommand("mkdir -p $dir")
                device.executeShellCommand("chmod 777 $dir")
            }
            Thread.sleep(200)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected fun saveScreenshot(name: String): File? {
        return try {
            ensurePermissions()
            createDirectoriesIfNeeded()

            val screenshotsDir = File("/sdcard/screenshots")
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs()
            }

            val file = File(screenshotsDir, "$name.png")
            device.takeScreenshot(file)

            if (file.exists()) {
                file
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

