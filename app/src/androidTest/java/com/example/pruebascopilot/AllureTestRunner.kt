package com.example.pruebascopilot

import androidx.test.runner.AndroidJUnitRunner

class AllureTestRunner : AndroidJUnitRunner() {

    override fun onStart() {
        prepareDynamicsPropertiesForAllure()
        super.onStart()
    }

    private fun prepareDynamicsPropertiesForAllure() {
        try {
            val targetContext = targetContext
            val cacheDir = targetContext.externalCacheDir
            if (cacheDir != null) {
                val allureResultsDir = "${cacheDir.absolutePath}/allure-results"
                System.setProperty("allure.results.directory", allureResultsDir)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

