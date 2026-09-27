plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.touristapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.touristapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 🔐 API KEYS (SECURE)
        buildConfigField("String", "OPENAI_API_KEY", "\"${project.findProperty("OPENAI_API_KEY") ?: ""}\"")
        buildConfigField("String", "GEMINI_API_KEY", "\"${project.findProperty("GEMINI_API_KEY") ?: ""}\"")
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

    buildFeatures {
        buildConfig = true   // 🔥 IMPORTANT
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // 🔥 Firebase BOM
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))

    // Core UI
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.cardview)

    // 🗺️ Google Maps
    implementation(libs.play.services.maps)

    // 📍 Location
    implementation(libs.play.services.location)

    // 🔐 Firebase Auth
    implementation("com.google.firebase:firebase-auth")

    // Google base
    implementation("com.google.android.gms:play-services-base:18.5.0")

    // 🔥 BLOCKCHAIN (Web3)
    implementation("org.web3j:core:4.9.8")

    // 🤖 AI (Networking)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
