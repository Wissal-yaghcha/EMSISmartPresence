plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin) // Firebase Plugin
}

android {
    namespace = "com.example.emsismartpresence"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.emsismartpresence"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.google.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Firebase dependencies
    implementation(platform(libs.firebase.bom)) // Firebase BOM (Bill of Materials)

    // Firebase Authentication
    implementation(libs.firebase.auth)

    // Firebase Firestore (optional, if you're using it)
    implementation(libs.firebase.firestore)
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    // Add other Firebase products if needed
}
