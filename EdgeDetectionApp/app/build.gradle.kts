plugins {
    // Use function call syntax id(...) with double quotes
    id("com.android.application")
    // The Kotlin plugin ID must be on its own line
    id("org.jetbrains.kotlin.android")
}

android {
    // Use '=' for assignment and " for strings
    namespace = "com.example.edgedetectionapp"
    compileSdk = 36

    defaultConfig {
        // Use '=' for assignment and " for strings
        applicationId = "com.example.edgedetectionapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            // Use .addAll() with a list created by listOf()
            abiFilters.addAll(listOf("arm64-v8a", "armeabi-v7a"))
        }
    }

    buildTypes {
        // Use getByName(...) to configure the release block
        getByName("release") {
            isMinifyEnabled = false
            // Use function call syntax with parentheses
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        // These are already correct
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    // THIS IS THE CORRECTED BLOCK
    // It replaces the old kotlinOptions block
    kotlin {
        jvmToolchain(8)
    }

    externalNativeBuild {
        cmake {
            // Use file(...) for paths, '=' for assignment, and " for strings
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    // It's good practice to include buildFeatures for things like ViewBinding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Replaced all version catalog aliases with full dependency strings
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // OpenCV dependency
    implementation("org.opencv:opencv-android:4.8.0")

    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
