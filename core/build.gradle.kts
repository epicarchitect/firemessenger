plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 31

    defaultConfig {
        minSdk = 26
        targetSdk = 31
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"))
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(project(":domain"))
    api("androidx.core:core-ktx:1.7.0")
    api("androidx.appcompat:appcompat:1.4.1")
    api("androidx.activity:activity-compose:1.4.0")
    api("androidx.compose.material:material:1.1.1")
    api("androidx.compose.material:material-icons-extended:1.1.1")
    api("androidx.navigation:navigation-compose:2.4.2")
    api("io.insert-koin:koin-core:3.1.4")
    api("io.insert-koin:koin-android:3.1.4")
}