plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.edgedetectionapp"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.example.edgedetectionapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        ndk {
            abiFilters.addAll(listOf("arm64-v8a", "armeabi-v7a"))
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    externalNativeBuild {
        cmake {
            // Use file() to convert the String path to a File object
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }

}
dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // OpenCV (add after Step 4)
}
