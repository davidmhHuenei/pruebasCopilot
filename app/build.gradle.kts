plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.pruebascopilot"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.pruebascopilot"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.pruebascopilot.AllureTestRunner"
        testInstrumentationRunnerArguments["listener"] = "com.example.pruebascopilot.TestListener"
    }

    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.barista)
    androidTestImplementation(libs.allure)
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")
    androidTestUtil(libs.orchestrator)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

tasks.register<Exec>("pullScreenshots") {
    commandLine("cmd", "/c", "adb shell test -d /sdcard/screenshots && adb pull /sdcard/screenshots \"QA evidencias\"")
}

tasks.register<Exec>("pullAllureResults") {
    commandLine("cmd", "/c", "adb pull /sdcard/Android/data/com.example.pruebascopilot/cache/allure-results app/build/allure-results")
}

tasks.register<Exec>("generateAllureReport") {
    commandLine("cmd", "/c", "allure generate app/build/allure-results --clean -o allure-report")
}

afterEvaluate {
    tasks.findByName("connectedAndroidTest")?.finalizedBy("pullScreenshots", "pullAllureResults", "generateAllureReport")
}
